package com.highfive.artary.service;

import com.highfive.artary.domain.Diary;
import com.highfive.artary.domain.Sticker;
import com.highfive.artary.domain.Summary;
import com.highfive.artary.domain.User;
import com.highfive.artary.dto.diary.DiaryRequestDto;
import com.highfive.artary.dto.diary.DiaryResponseDto;
import com.highfive.artary.dto.sticker.StickerResponseDto;
import com.highfive.artary.repository.DiaryRepository;
import com.highfive.artary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
    public List<DiaryResponseDto> getDiaries(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        List<Diary> diaries = user.getDiaries();
        List<DiaryResponseDto> diaryResponseDtos = new ArrayList<>();

        for (Diary diary : diaries) {
            DiaryResponseDto diaryResponseDto = new DiaryResponseDto(diary);
            diaryResponseDtos.add(diaryResponseDto);
        }

        return diaryResponseDtos;
    }

    @Override
    public List<StickerResponseDto> getStickers(Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId).orElseThrow(() ->
                new IllegalArgumentException("해당 일기가 존재하지 않습니다."));

        List<Sticker> stickers = diary.getStickers();
        List<StickerResponseDto> stickerResponseDtos = new ArrayList<>();

        for (Sticker sticker : stickers) {
            StickerResponseDto stickerResponseDto = new StickerResponseDto(sticker);
            stickerResponseDtos.add(stickerResponseDto);
        }

        return stickerResponseDtos;
    }

    @Override
    public Long save(DiaryRequestDto requestDto, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        return diaryRepository.save(requestDto.toEntity(user)).getId();
    }

    @Override
    public void setSummary(Long diary_id, String koSummary, String engSummary) {
        Diary diary = diaryRepository.findById(diary_id).orElseThrow(() ->
                new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));

        Summary summary = diary.getSummary();
        if (summary == null) {
            summary = new Summary();
            summary.setDiary(diary);
        }

        summary.setKoSummary(koSummary);
        summary.setEngSummary(engSummary);

        diary.setSummary(summary);
        diaryRepository.save(diary);
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
