package com.highfive.artary.domain;

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

    @OneToOne
    @JoinColumn(name = "user_id")
    @NonNull
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id")
    @NonNull
    private Diary diary;

    @Column(name = "sticker_type")
    @NonNull
    private StickerType type;

    @Column(name = "img")
    @NonNull
    private String image;

    @Builder
    public Sticker(User user, Diary diary, StickerType type, String image) {
        this.user = user;
        this.diary = diary;
        this.type =  type;
        this.image = image;
    }
}
