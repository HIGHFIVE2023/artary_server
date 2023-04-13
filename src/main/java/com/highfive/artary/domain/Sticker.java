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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id")
    @NonNull
    private Diary diary;

    @Column(name = "sticker_name")
    @NonNull
    private String name;

    @Column(name = "img")
    @NonNull
    private String image;

}
