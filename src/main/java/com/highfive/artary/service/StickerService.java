package com.highfive.artary.service;

import com.highfive.artary.dto.sticker.StickerRequestDto;
import com.highfive.artary.dto.sticker.StickerResponseDto;

import java.util.List;

public interface StickerService {

    Long save(Long diary_id, Long user_id, StickerRequestDto requestDto);

    List<StickerResponseDto> findAllByDiary(Long diary_id);

    void update(Long sticker_id, StickerRequestDto requestDto);

    void delete(Long sticker_id);
}
