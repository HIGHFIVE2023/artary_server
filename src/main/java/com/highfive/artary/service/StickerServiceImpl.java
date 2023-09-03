package com.highfive.artary.service;

import com.highfive.artary.domain.Diary;
import com.highfive.artary.domain.NotificationType;
import com.highfive.artary.domain.Sticker;
import com.highfive.artary.domain.User;
import com.highfive.artary.dto.sticker.StickerRequestDto;
import com.highfive.artary.dto.sticker.StickerResponseDto;
import com.highfive.artary.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StickerServiceImpl implements StickerService {

    private final UserRepository userRepository;
    private final DiaryRepository diaryRepository;
    private final StickerRepository stickerRepository;

    private final NotificationService notificationService;

    @Override
    public List<StickerResponseDto> findAllByDiary(Long diary_id) {
        Diary diary = diaryRepository.findById(diary_id).orElseThrow(() ->
                new IllegalArgumentException("스티커 조회 실패: 해당 일기가 존재하지 않습니다."));

        List<Sticker> stickers = diary.getStickers();

        return stickers.stream().map(StickerResponseDto::new).collect(Collectors.toList());
    }

    @Override
    public Long save(Long diary_id, Long user_id, StickerRequestDto requestDto) {
        User user = userRepository.findById(user_id).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
        Diary diary = diaryRepository.findById(diary_id).orElseThrow(() ->
                new IllegalArgumentException("해당 일기가 존재하지 않습니다."));

        User receiver = diary.getUser();
        Sticker sticker = requestDto.toEntity(user, diary);
        stickerRepository.save(sticker);

        // 알림 생성
        String content = user.getNickname() + "님이 '" + diary.getTitle() + "' 게시물에 스티커를 남겼습니다.";
        String url = "http://localhost:3000/diary/" + diary.getId();

        notificationService.send(receiver, NotificationType.DIARY, content, url);

        return sticker.getId();
    }

    @Override
    public void update(Long sticker_id, StickerRequestDto requestDto) {
        Sticker sticker = stickerRepository.findById(sticker_id).orElseThrow(() ->
                new IllegalArgumentException("해당 스티커가 존재하지 않습니다."));

        sticker.update(requestDto.getType());
    }

    @Override
    public void delete(Long sticker_id) {
        Sticker sticker = stickerRepository.findById(sticker_id).orElseThrow(() ->
                new IllegalArgumentException("해당 스티커가 존재하지 않습니다."));

        stickerRepository.delete(sticker);
    }
}
