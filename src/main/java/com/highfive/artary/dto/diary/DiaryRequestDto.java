package com.highfive.artary.dto.diary;

import com.highfive.artary.domain.Diary;
import com.highfive.artary.domain.Emotion;
import com.highfive.artary.domain.User;
import lombok.*;

@NoArgsConstructor
@Getter
public class DiaryRequestDto {

    private String title;
    private String content;
    private String image;
    private Emotion emotion;

    public Diary toEntity(User user) {
        return Diary.builder()
                .user(user)
                .title(title)
                .content(content)
                .image(image)
                .emotion(emotion)
                .build();
    }
}
