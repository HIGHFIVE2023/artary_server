package com.highfive.artary.service;

import com.highfive.artary.domain.Diary;
import com.highfive.artary.domain.Emotion;
import com.highfive.artary.domain.User;
import com.highfive.artary.dto.diary.DiaryRequestDto;
import com.highfive.artary.dto.diary.DiaryResponseDto;
import com.highfive.artary.repository.DiaryRepository;

public interface DiaryService {

    DiaryResponseDto getById(Long diary_id);

    // 일기 저장
    Long save(DiaryRequestDto requestDto, Long user_id);

    // 일기 요약 & 번역
    void setSummary(Long diary_id, String koSummary, String engSummary);

    // 일기 수정
    void update(DiaryRequestDto requestDto, Long user_id, Long diary_id);

    // 일기 삭제
    void delete(Long diary_id);
}
