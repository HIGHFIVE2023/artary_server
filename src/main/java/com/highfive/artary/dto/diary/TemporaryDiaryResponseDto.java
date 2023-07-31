package com.highfive.artary.dto.diary;

import com.highfive.artary.domain.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class TemporaryDiaryResponseDto {
    private final Long id;
    private final User user;
    private final String title;
    private final String content;
    private final String image;
    private final Emotion emotion;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public TemporaryDiaryResponseDto(TemporaryDiary diary) {
        this.id = diary.getId();
        this.user = diary.getUser();
        this.title = diary.getTitle();
        this.content = diary.getContent();
        this.image = diary.getImage();
        this.emotion = diary.getEmotion();
        this.createdAt = diary.getCreatedAt();
        this.updatedAt = diary.getUpdatedAt();
    }
}
