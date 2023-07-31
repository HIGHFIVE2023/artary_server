package com.highfive.artary.service;

import com.highfive.artary.dto.diary.DiaryRequestDto;
import com.highfive.artary.dto.diary.DiaryResponseDto;
import com.highfive.artary.dto.sticker.StickerResponseDto;

import java.util.List;

public interface DiaryService {

    DiaryResponseDto getById(Long diary_id);

    List<DiaryResponseDto> getDiaries(String email);

    List<StickerResponseDto> getStickers(Long diaryId);

    // 일기 저장
    Long save(Long diary_id, String email);

    // 일기 수정
    void update(DiaryRequestDto requestDto, Long user_id, Long diary_id);

    // 일기 삭제
    void delete(Long diary_id);
}
