package com.highfive.artary.controller;

import com.highfive.artary.domain.User;
import com.highfive.artary.dto.sticker.StickerRequestDto;
import com.highfive.artary.service.StickerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/diary")
public class StickerController {

    private final StickerService stickerService;

    @PostMapping("/{diary_id}/sticker")
    public ResponseEntity save(@PathVariable Long diary_id, @RequestBody StickerRequestDto requestDto, @AuthenticationPrincipal User user) {
        Long user_id = user.getId();
        stickerService.save(diary_id, user_id, requestDto);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("/{diary_id}/sticker/{sticker_id}")
    public ResponseEntity update(@PathVariable("sticker_id") Long sticker_id, @RequestBody StickerRequestDto requestDto) {
        stickerService.update(sticker_id, requestDto);

        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/{diary_id}/sticker/{sticker_id}")
    public ResponseEntity delete(@PathVariable("sticker_id") Long sticker_id) {
        stickerService.delete(sticker_id);

        return new ResponseEntity(HttpStatus.OK);
    }
}
