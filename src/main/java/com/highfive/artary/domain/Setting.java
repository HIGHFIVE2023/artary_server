package com.highfive.artary.domain;

import javax.persistence.*;

@Entity
@Table(name = "setting")
public class Setting {

    @Id
    @Column(name = "user_id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "bg_color")
    private String backColor;

    @Column(name = "bg_img")
    private String backImage;

    @Column(name = "cover_color")
    private String coverColor;

    @Column(name = "cover_img")
    private String coverImage;
}
