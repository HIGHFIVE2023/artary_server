package com.highfive.artary.service;

import com.highfive.artary.domain.Diary;
import com.highfive.artary.dto.diary.DiaryResponseDto;
import com.highfive.artary.dto.sticker.StickerResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DiaryService {

    DiaryResponseDto getById(Long diary_id);

    List<DiaryResponseDto> getDiaries(String email);

    List<DiaryResponseDto> getDiariesByUserId(Long userId);

    List<DiaryResponseDto> getDiariesByUserNickname(String nickname);

    Page<Diary> getPageDiaries(String email, int page);

    List<StickerResponseDto> getStickers(Long diaryId);

    // 일기 저장
    Long save(Long diary_id, String email);

    // 일기 수정
    void update(Long diary_id);

    // 일기 삭제
    void delete(Long diary_id);

    Long getUserIdByDiaryId(Long diary_id);

    // 접근 권한 체크
    boolean checkPermissionToAccessDiary(String email, Long diary_d);
    boolean checkPermissionToAccessList(String email, String nickname);
}
