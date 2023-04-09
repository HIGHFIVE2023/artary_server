package com.highfive.artary.domain;

import javax.validation.constraints.NotNull;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_name")
    @NotNull
    private String name;

    @NotNull
    private String nickname;

    @NotNull
    private String password;

    @NotNull
    private String email;

    @Column(name = "profile_img")
    private String image;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Diary> diary = new ArrayList<>();
}
