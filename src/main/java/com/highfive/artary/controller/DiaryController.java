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

import java.util.List;

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
    public String saveDiary(@Validated @RequestBody DiaryRequestDto diaryDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            // 사용자가 인증되지 않은 경우 처리
        }
        UserDetails userDetails = userService.loadUserByUsername(authentication.getName());
        Long userId = ((User) userDetails).getId();

        Long savedId = diaryService.save(diaryDto, userId);

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
    public ResponseEntity<byte[]> getPicture(@PathVariable Long diary_id) {
        // 요약 및 번역
        String summary = clovaSummaryService.summarizeDiary(diary_id);
        String engSummary = papagoTranslationService.translateSummary(diary_id);

        diaryService.setSummary(diary_id, summary, engSummary);

        byte[] imageBytes = stablediffusionService.getTextToImage(diary_id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentLength(imageBytes.length);

        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }
}