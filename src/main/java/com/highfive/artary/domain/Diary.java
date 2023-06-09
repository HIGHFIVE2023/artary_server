package com.highfive.artary.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@ToString(callSuper = true, exclude = "user")
@EqualsAndHashCode(callSuper = true)
@Table(name = "diary")
public class Diary extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;

    @NotEmpty
    private String title;

    @NotEmpty
    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "image_url")
    private String image;

    @NonNull
    private Emotion emotion;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "summary_id")
    private Summary summary;

    @JsonIgnore
    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sticker> stickers = new ArrayList<>();

    @Builder
    public Diary(User user, String title, String content, String image, Emotion emotion) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.image = image;
        this.emotion = emotion;
    }

    public void update(String title, String content, String image, Emotion emotion) {
        this.title = title;
        this.content = content;
        this.image = image;
        this.emotion = emotion;
    }

    public void addSticker(Sticker sticker) {
        stickers.add(sticker);
    }
}
