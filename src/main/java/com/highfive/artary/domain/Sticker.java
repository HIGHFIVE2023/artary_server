package com.highfive.artary.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "sticker")
public class Sticker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sticker_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id")
    @NotNull
    private Diary diary;

    @Column(name = "sticker_name")
    @NotNull
    private String name;

    @Column(name = "img")
    @NotNull
    private String image;

    @Column(name = "created_at")
    @CreatedDate
    @NotNull
    private LocalDateTime createDate;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updateDate;
}
