package com.highfive.artary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.highfive.artary.domain.Diary;
import com.highfive.artary.domain.FirstSentence;
import com.highfive.artary.domain.User;
import com.highfive.artary.dto.diary.*;
import com.highfive.artary.dto.sticker.StickerResponseDto;
import com.highfive.artary.dto.textGeneration.FirstSentenceRequestDto;
import com.highfive.artary.service.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import javax.validation.constraints.Positive;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("diary")
public class DiaryController {

    @Autowired
    private final DiaryService diaryService;
    @Autowired
    private final TemporaryDiaryService temporaryDiaryService;
    @Autowired
    private final StickerService stickerService;
    @Autowired
    private final StableDiffusionService stablediffusionService;
    @Autowired
    private final ClovaSummaryService clovaSummaryService;
    @Autowired
    private final PapagoTranslationService papagoTranslationService;
    @Autowired
    private final FirstSentenceService firstSentenceService;

    // 임시 저장
    @PostMapping("/write")
    public ResponseEntity<Long> saveTemporaryDiary(@Validated @RequestBody TemporaryDiaryRequestDto diaryDto, @AuthenticationPrincipal String email) {
        Long savedId = temporaryDiaryService.save(diaryDto, email);

        return ResponseEntity.ok(savedId);
    }

    // Diary 저장
    @PostMapping("/{diary_id}/save")
    public String saveDiary(@PathVariable Long diary_id, @AuthenticationPrincipal String email) {
        Long savedId = diaryService.save(diary_id, email);

        return "redirect:/diary/" + savedId;
    }

    @GetMapping("/{diary_id}")
    public ResponseEntity<?> getDiary(@PathVariable Long diary_id, Model model) {
        DiaryResponseDto diaryResponseDto = diaryService.getById(diary_id);
        model.addAttribute("diary", diaryResponseDto);

        List<StickerResponseDto> stickerListDto = stickerService.findAllByDiary(diary_id);
        model.addAttribute("stickerList", stickerListDto);

        return new ResponseEntity<>(diaryService.getById(diary_id), HttpStatus.OK);
    }

    @GetMapping("/temporary/{diary_id}")
    public ResponseEntity<?> getTemporaryDiary(@PathVariable Long diary_id, Model model) {
        TemporaryDiaryResponseDto temporaryDiaryResponseDto = temporaryDiaryService.getById(diary_id);
        model.addAttribute("diary", temporaryDiaryResponseDto);

        return new ResponseEntity<>(temporaryDiaryService.getById(diary_id), HttpStatus.OK);
    }

    @GetMapping("/diaries")
    public ResponseEntity<?> getDiaries(@AuthenticationPrincipal String email) {
        List<DiaryResponseDto> diaryResponseDtos = diaryService.getDiaries(email);

        return new ResponseEntity<>(diaryResponseDtos, HttpStatus.OK);
    }

    // 페이지네이션
    @GetMapping("/pagination/{nickname}")
    public ResponseEntity<?> getPageDiaries(@PathVariable String nickname, @Positive @RequestParam int page) {
        Page<Diary> diaryPage = diaryService.getPageDiaries(nickname, page);
        PageInfo pageInfo = new PageInfo(page, 4, (int) diaryPage.getTotalElements(), diaryPage.getTotalPages());
        List<Diary> diaries = diaryPage.getContent();

        return new ResponseEntity<>(new DiaryAllDto(diaries, pageInfo), HttpStatus.OK);
    }

    @GetMapping("/{diary_id}/stickers")
    public ResponseEntity<?> getStickers(@PathVariable Long diary_id) {
        List<StickerResponseDto> stickerResponseDtos = diaryService.getStickers(diary_id);

        return new ResponseEntity<>(stickerResponseDtos, HttpStatus.OK);
    }

    @PutMapping("/{diary_id}")
    public ResponseEntity updateDiary(@RequestBody TemporaryDiaryRequestDto requestDto, @PathVariable Long diary_id) {
        temporaryDiaryService.update(requestDto, diary_id);

        return ResponseEntity.ok().body("{\"message\": \"수정 성공\"}");
    }

    @PutMapping("/{diary_id}/edit")
    public ResponseEntity editDiary(@PathVariable Long diary_id) {
        diaryService.update(diary_id);

        return ResponseEntity.ok().body("{\"message\": \"수정 성공\"}");
    }

    @DeleteMapping("/{diary_id}")
    public ResponseEntity deleteDiary(@PathVariable Long diary_id) {
        diaryService.delete(diary_id);
        temporaryDiaryService.delete(diary_id);

        return ResponseEntity.ok().body("{\"message\": \"삭제 성공\"}");
    }

    @GetMapping("/{diary_id}/picture/paint")
    public ResponseEntity<?> getPictureV1(@PathVariable Long diary_id) {
        return getPictureResponse(diary_id, "V1");
    }

    @GetMapping("/{diary_id}/picture/pencil")
    public ResponseEntity<?> getPictureV2(@PathVariable Long diary_id) {
        return getPictureResponse(diary_id, "V2");
    }

    private ResponseEntity<?> getPictureResponse(Long diary_id, String version) {
        int maxAttempts = 10; // 최대 재시도 횟수
        int attempts = 0;

        while (attempts < maxAttempts) {
            try {
                // 요약 및 번역
                String summary = clovaSummaryService.summarizeDiary(diary_id);
                String engSummary = papagoTranslationService.translateSummary(diary_id);

                temporaryDiaryService.setSummary(diary_id, summary, engSummary);

                String imageUrl = version.equals("V1") ?
                        stablediffusionService.getTextToImageV1(diary_id) :
                        stablediffusionService.getTextToImageV2(diary_id);

                Map<String, Object> response = new HashMap<>();
                response.put("imageUrl", imageUrl);

                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch (WebClientRequestException e) {
                // 재시도 로직을 위해 예외 처리
                attempts++;
                if (attempts >= maxAttempts) {
                    // 최대 재시도 횟수를 초과한 경우
                    return new ResponseEntity<>("Failed to get the picture.", HttpStatus.INTERNAL_SERVER_ERROR);
                }
                // 일시적인 연결 문제로 예외가 발생한 경우, 대기 후 재시도
                try {
                    Thread.sleep(1000); // 1초 대기 후 재시도
                } catch (InterruptedException ex) {
                    // 대기 도중 인터럽트가 발생한 경우 예외 처리
                    Thread.currentThread().interrupt();
                    return new ResponseEntity<>("Failed to get the picture.", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } catch (Exception e) {
                // 다른 예외가 발생한 경우
                return new ResponseEntity<>("Failed to get the picture.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        // 최대 재시도 횟수를 초과한 경우
        return new ResponseEntity<>("Failed to get the picture.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/firstSentence")
    public ResponseEntity<?> createFirstSentence(@RequestBody FirstSentenceRequestDto requestDto) {
        log.info("Input Words: {}", requestDto.getInputWords());

        try {
            FirstSentence savedSentence = firstSentenceService.save(requestDto);
            firstSentenceService.generateText(savedSentence.getId());
            return ResponseEntity.ok(savedSentence);
        } catch (Exception e) {
            log.error("Error occurred while creating the first sentence", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}