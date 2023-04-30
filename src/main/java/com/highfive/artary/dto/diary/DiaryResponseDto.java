package com.highfive.artary.dto.diary;

import com.highfive.artary.domain.Diary;
import com.highfive.artary.domain.Emotion;
import com.highfive.artary.domain.Sticker;
import com.highfive.artary.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class DiaryResponseDto {
    private final Long id;
    private final User user;
    private final String title;
    private final String content;
    private final String image;
    private final Emotion emotion;
    private final List<Sticker> stickers;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public DiaryResponseDto(Diary diary) {
        this.id = diary.getId();
        this.user = diary.getUser();
        this.title = diary.getTitle();
        this.content = diary.getContent();
        this.image = diary.getImage();
        this.emotion = diary.getEmotion();
        this.stickers = diary.getStickers();
        this.createdAt = diary.getCreatedAt();
        this.updatedAt = diary.getUpdatedAt();
    }
}
