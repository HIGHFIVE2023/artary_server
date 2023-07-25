package com.highfive.artary.controller;

import com.highfive.artary.domain.User;
import com.highfive.artary.dto.diary.DiaryRequestDto;
import com.highfive.artary.dto.diary.DiaryResponseDto;
import com.highfive.artary.dto.sticker.StickerResponseDto;
import com.highfive.artary.service.*;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("diary")
public class DiaryController {

    @Autowired
    private final UserService userService;
    @Autowired
    private final DiaryService diaryService;
    @Autowired
    private final StickerService stickerService;
    @Autowired
    private final StableDiffusionService stablediffusionService;
    @Autowired
    private final ClovaSummaryService clovaSummaryService;
    @Autowired
    private final PapagoTranslationService papagoTranslationService;

    @PostMapping("/write")
    public String saveDiary(@Validated @RequestBody DiaryRequestDto diaryDto, @AuthenticationPrincipal String email) {
        Long savedId = diaryService.save(diaryDto, email);

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

    @GetMapping("/diaries")
    public ResponseEntity<?> getDiaries(@AuthenticationPrincipal String email) {
        List<DiaryResponseDto> diaryResponseDtos = diaryService.getDiaries(email);

        return new ResponseEntity<>(diaryResponseDtos, HttpStatus.OK);
    }

    @PutMapping("/{diary_id}")
    public ResponseEntity updateDiary(@RequestBody DiaryRequestDto diaryDto, @PathVariable Long diary_id, User user) {
        Long userId = user.getId();
        diaryService.update(diaryDto, userId, diary_id);

        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/{diary_id}")
    public ResponseEntity deleteDiary(@PathVariable Long diary_id) {
        diaryService.delete(diary_id);

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/{diary_id}/picture")
    public ResponseEntity<?> getPicture(@PathVariable Long diary_id) {
        int maxAttempts = 10; // 최대 재시도 횟수
        int attempts = 0;

        while (attempts < maxAttempts) {
            try {
                // 요약 및 번역
                String summary = clovaSummaryService.summarizeDiary(diary_id);
                String engSummary = papagoTranslationService.translateSummary(diary_id);

                diaryService.setSummary(diary_id, summary, engSummary);

                String imageUrl = stablediffusionService.getTextToImage(diary_id);

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
}