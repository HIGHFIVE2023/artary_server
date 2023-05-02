package com.highfive.artary.dto.sticker;

import com.highfive.artary.domain.Diary;
import com.highfive.artary.domain.Sticker;
import com.highfive.artary.domain.StickerType;
import com.highfive.artary.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class StickerRequestDto {
    private Long id;
    private Diary diary;
    private User user;
    private StickerType type;

    private int xCoordinate;

    private int yCoordinate;

    public void setDiary(Diary diary) {
        this.diary = diary;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Sticker toEntity(User user, Diary diary) {
        Sticker sticker = Sticker.builder()
                .diary(diary)
                .user(user)
                .type(type)
                .xCoordinate(xCoordinate)
                .yCoordinate(yCoordinate)
                .build();

        return sticker;
    }
}
