package com.highfive.artary.service;

import com.highfive.artary.domain.Diary;
import com.highfive.artary.domain.Summary;
import com.highfive.artary.domain.TemporaryDiary;
import com.highfive.artary.domain.User;
import com.highfive.artary.dto.diary.DiaryRequestDto;
import com.highfive.artary.dto.diary.DiaryResponseDto;
import com.highfive.artary.dto.diary.TemporaryDiaryRequestDto;
import com.highfive.artary.dto.diary.TemporaryDiaryResponseDto;
import com.highfive.artary.repository.TemporaryDiaryRepository;
import com.highfive.artary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TemporaryDiaryServiceImpl implements TemporaryDiaryService {

    private final TemporaryDiaryRepository temporaryDiaryRepository;
    private final UserRepository userRepository;

    @Override
    public TemporaryDiaryResponseDto getById(Long diary_id) {
        TemporaryDiary diary = temporaryDiaryRepository.findById(diary_id).orElseThrow(() ->
                new IllegalArgumentException("해당 일기가 존재하지 않습니다."));

        return new TemporaryDiaryResponseDto(diary);
    }

    @Override
    public Long save(TemporaryDiaryRequestDto requestDto, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        return temporaryDiaryRepository.save(requestDto.toEntity(user)).getId();
    }

    @Override
    public void setSummary(Long diary_id, String koSummary, String engSummary) {
        TemporaryDiary diary = temporaryDiaryRepository.findById(diary_id).orElseThrow(() ->
                new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));

        Summary summary = diary.getSummary();
        if (summary == null) {
            summary = new Summary();
            summary.setDiary(diary);
        }

        summary.setKoSummary(koSummary);
        summary.setEngSummary(engSummary);

        diary.setSummary(summary);
        temporaryDiaryRepository.save(diary);
    }

    @Override
    public void delete(Long diary_id) {
        TemporaryDiary diary = temporaryDiaryRepository.findById(diary_id).orElseThrow(() ->
                new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));
        temporaryDiaryRepository.delete(diary);
    }
}
