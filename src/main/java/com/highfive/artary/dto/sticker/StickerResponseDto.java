package com.highfive.artary.dto.sticker;

import com.highfive.artary.domain.Diary;
import com.highfive.artary.domain.Sticker;
import com.highfive.artary.domain.StickerType;
import com.highfive.artary.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class StickerResponseDto {

    private final Long id;
    private final Diary diary;
    private final User user;
    private final StickerType type;

    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public StickerResponseDto(Sticker sticker) {
        this.id = sticker.getId();
        this.diary = sticker.getDiary();
        this.user = sticker.getUser();
        this.type = sticker.getType();
        this.createdAt = sticker.getCreatedAt();
        this.updatedAt = sticker.getUpdatedAt();
    }
}
