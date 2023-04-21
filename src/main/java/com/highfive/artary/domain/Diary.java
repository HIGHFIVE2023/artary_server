package com.highfive.artary.domain;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "diary")
public class Diary extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotEmpty
    private String title;

    @NotEmpty
    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "image_url")
    @NotEmpty
    private String image;

    @NonNull
    private Emotion emotion;

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

    public void addSticker(Sticker sticker) {
        stickers.add(sticker);
    }
}
