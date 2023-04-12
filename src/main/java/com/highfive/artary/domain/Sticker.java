package com.highfive.artary.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
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
    private Diary diary;

    @Column(name = "sticker_name")
    private String name;

    @Column(name = "img")
    private String image;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createDate;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updateDate;
}
