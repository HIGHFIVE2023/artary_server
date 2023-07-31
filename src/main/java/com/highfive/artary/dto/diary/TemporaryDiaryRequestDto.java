package com.highfive.artary.dto.diary;

import com.highfive.artary.domain.Emotion;
import com.highfive.artary.domain.TemporaryDiary;
import com.highfive.artary.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TemporaryDiaryRequestDto {

    private String title;
    private String content;
    private String image;
    private Emotion emotion;

    public TemporaryDiary toEntity(User user) {
        return TemporaryDiary.builder()
                .user(user)
                .title(title)
                .content(content)
                .image(image)
                .emotion(emotion)
                .build();
    }
}
