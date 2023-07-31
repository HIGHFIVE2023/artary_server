package com.highfive.artary.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true, exclude = "user")
@Table(name = "diary")
public class Diary extends BaseDiary{

    @Id
    @Column(name = "diary_id")
    private Long id;

    @JsonIgnore
    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sticker> stickers = new ArrayList<>();

    @Builder
    public Diary(Long id, User user, String title, String content, String image, Emotion emotion) {
        super(user, title, content, image, emotion);
        this.id = id;
    }

    public void addSticker(Sticker sticker) {
        stickers.add(sticker);
    }
}
