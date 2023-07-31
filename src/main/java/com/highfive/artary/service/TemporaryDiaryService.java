package com.highfive.artary.service;

import com.highfive.artary.dto.diary.TemporaryDiaryRequestDto;
import com.highfive.artary.dto.diary.TemporaryDiaryResponseDto;

public interface TemporaryDiaryService {

    TemporaryDiaryResponseDto getById(Long diary_id);
    Long save(TemporaryDiaryRequestDto requestDto, String email);
    void setSummary(Long diary_id, String koSummary, String engSummary);
    void delete(Long diary_id);
}
