package com.highfive.artary.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, exclude = {"user", "stickers"})
@Table(name = "diary")
public class Diary extends BaseDiary{

    @Id
    @Column(name = "diary_id")
    private Long id;

    @JsonIgnore
    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sticker> stickers = new ArrayList<>();

    private String bgm;

    @Builder
    public Diary(Long id, User user, String title, String content, String image, Emotion emotion, String bgm) {
        super(user, title, content, image, emotion);
        this.id = id;
        this.bgm = bgm;
    }

    public void addSticker(Sticker sticker) {
        stickers.add(sticker);
    }

    public void updateBgm(String bgm) {
        this.bgm = bgm;
    }
}
