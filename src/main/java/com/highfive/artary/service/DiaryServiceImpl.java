package com.highfive.artary.service;

import com.highfive.artary.domain.*;
import com.highfive.artary.dto.diary.DiaryResponseDto;
import com.highfive.artary.dto.sticker.StickerResponseDto;
import com.highfive.artary.repository.DiaryRepository;
import com.highfive.artary.repository.FriendRepository;
import com.highfive.artary.repository.TemporaryDiaryRepository;
import com.highfive.artary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {
    private final DiaryRepository diaryRepository;
    private final TemporaryDiaryRepository temporaryDiaryRepository;
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;
    private final RiffusionService riffusionService;

    @Override
    public DiaryResponseDto getById(Long diary_id) {
        Diary diary = getDiaryById(diary_id);

        return new DiaryResponseDto(diary);
    }

    @Override
    public List<DiaryResponseDto> getDiaries(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        return convertToResponseDto(user.getDiaries());
    }

    @Override
    public List<DiaryResponseDto> getDiariesByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        return convertToResponseDto(user.getDiaries());
    }

    @Override
    public List<DiaryResponseDto> getDiariesByUserNickname(String nickname) {
        User user = userRepository.findByNickname(nickname).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        return convertToResponseDto(user.getDiaries());
    }

    private List<DiaryResponseDto> convertToResponseDto(List<Diary> diaries) {
        List<DiaryResponseDto> diaryResponseDtos = new ArrayList<>();

        for (Diary diary : diaries) {
            DiaryResponseDto diaryResponseDto = new DiaryResponseDto(diary);
            diaryResponseDtos.add(diaryResponseDto);
        }

        return diaryResponseDtos;
    }

    @Override
    public Page<Diary> getPageDiaries(String nickname, int page) {
        PageRequest pageRequest = PageRequest.of(page - 1, 4);
        User user = userRepository.findByNickname(nickname).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        return diaryRepository.findByUserOrderByIdDesc(user, pageRequest);
    }

    @Override
    public List<StickerResponseDto> getStickers(Long diaryId) {
        Diary diary = getDiaryById(diaryId);

        List<Sticker> stickers = diary.getStickers();
        List<StickerResponseDto> stickerResponseDtos = new ArrayList<>();

        for (Sticker sticker : stickers) {
            StickerResponseDto stickerResponseDto = new StickerResponseDto(sticker);
            stickerResponseDtos.add(stickerResponseDto);
        }

        return stickerResponseDtos;
    }

    @Override
    public Long save(Long diary_id, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        TemporaryDiary temporaryDiary = temporaryDiaryRepository.findById(diary_id).orElseThrow(() ->
                new IllegalArgumentException("해당 데이터가 존재하지 않습니다."));

        String riffusionPrompt = temporaryDiary.getSummary().getEngSummary();
        String bgm = riffusionService.getAudio(riffusionPrompt);

        Diary diary = Diary.builder()
                .id(diary_id)
                .user(user)
                .title(temporaryDiary.getTitle())
                .content(temporaryDiary.getContent())
                .image(temporaryDiary.getImage())
                .emotion(temporaryDiary.getEmotion())
                .bgm(bgm)
                .build();

        return diaryRepository.save(diary).getId();
    }

    @Override
    public void update(Long diary_id) {
        Diary diary = getDiaryById(diary_id);

        TemporaryDiary temporaryDiary = temporaryDiaryRepository.findById(diary_id).orElseThrow(() ->
                new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));

        String riffusionPrompt = temporaryDiary.getSummary().getEngSummary();
        String bgm = riffusionService.getAudio(riffusionPrompt);

        diary.update(temporaryDiary.getTitle(), temporaryDiary.getContent(), temporaryDiary.getEmotion());
        diary.updateBgm(bgm);
        diary.updateImage(temporaryDiary.getImage());
    }

    @Override
    public void delete(Long diary_id) {
        Diary diary = getDiaryById(diary_id);
        diaryRepository.delete(diary);
    }

    @Override
    public Long getUserIdByDiaryId(Long diary_id) {
        Diary diary = getDiaryById(diary_id);
        Long user_id = diary.getUser().getId();

        return user_id;
    }

    @Override
    public boolean checkPermissionToAccessDiary(String email, Long diary_id) {
        Diary diary = getDiaryById(diary_id);
        User diaryUser = diary.getUser();
        List<Friend> friends = friendRepository.findAllByFromUserIdAndAreWeFriendOrToUserIdAndAreWeFriend(diaryUser, true, diaryUser, true);

        User loginUser = userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        if (diaryUser.getId().equals(loginUser.getId()) || isFriend(loginUser, friends)) {
            return true;
        }
        return false;
    }

    public boolean checkPermissionToAccessList(String email, String nickname) {
        User loginUser = userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        User user = userRepository.findByNickname(nickname).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        List<Friend> friends = friendRepository.findAllByFromUserIdAndAreWeFriendOrToUserIdAndAreWeFriend(user, true, user, true);

        if (user.getId().equals(loginUser.getId()) || isFriend(loginUser, friends)) {
            return true;
        }
        return false;
    }

    private Diary getDiaryById(Long diary_id) {
        Diary diary = diaryRepository.findById(diary_id).orElseThrow(() ->
                new IllegalArgumentException("해당 일기가 존재하지 않습니다."));

        return diary;
    }

    private boolean isFriend(User user, List<Friend> friends) {
        for (Friend friend : friends) {
            if (friend.getToUserId().getId().equals(user.getId()) || friend.getFromUserId().getId().equals(user.getId())) {
                return true;
            }
        }
        return false;
    }
}
