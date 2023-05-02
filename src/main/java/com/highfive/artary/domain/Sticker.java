package com.highfive.artary.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;



@Entity
@NoArgsConstructor
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "sticker")
public class Sticker extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sticker_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "diary_id")
    @NonNull
    private Diary diary;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "sticker", nullable = false)
    private StickerCategory type;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "x_coordinate")
    private int xCoordinate;

    @Column(name = "y_coordinate")
    private int yCoordinate;

    @Builder
    public Sticker(Diary diary, User user, StickerCategory type, int xCoordinate, int yCoordinate) {
        this.diary = diary;
        this.user = user;
        this.type = type;
        this.imageUrl = this.getType().getUrl();
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    public void update(StickerCategory type) {
        this.type = type;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

}
