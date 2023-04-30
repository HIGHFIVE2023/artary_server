package com.highfive.artary.service;

import com.highfive.artary.domain.Diary;
import com.highfive.artary.domain.User;
import com.highfive.artary.dto.diary.DiaryRequestDto;
import com.highfive.artary.dto.diary.DiaryResponseDto;
import com.highfive.artary.repository.DiaryRepository;
import com.highfive.artary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DiaryServiceImpl implements DiaryService {
    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;

    @Override
    public DiaryResponseDto getById(Long diary_id) {
        Diary diary = diaryRepository.findById(diary_id).orElseThrow(() ->
                new IllegalArgumentException("해당 일기가 존재하지 않습니다."));

        return new DiaryResponseDto(diary);
    }

    @Override
    public Long save(DiaryRequestDto requestDto, Long user_id) {
        User user = userRepository.findById(user_id).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        return diaryRepository.save(requestDto.toEntity(user)).getId();
    }

    @Override
    public void update(DiaryRequestDto requestDto, Long user_id, Long diary_id) {
        Diary diary = diaryRepository.findById(diary_id).orElseThrow(() ->
                new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));
        diary.update(requestDto.getTitle(), requestDto.getContent(), requestDto.getImage(), requestDto.getEmotion());
    }

    @Override
    public void delete(Long diary_id) {
        Diary diary = diaryRepository.findById(diary_id).orElseThrow(() ->
                new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));
        diaryRepository.delete(diary);
    }
}
