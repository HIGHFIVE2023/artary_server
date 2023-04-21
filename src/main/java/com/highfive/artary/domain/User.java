package com.highfive.artary.domain;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@ToString(callSuper = true, exclude = {"friends"})
@EqualsAndHashCode(callSuper = true)
@Table(name = "user")
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_name")
    @NonNull
    private String name;

    @NonNull
    private String nickname;

    @NonNull
    private String password;

    @NonNull
    @Email
    @Column(unique = true)
    private String email;

    @Column(name = "profile_img")
    private String image;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Diary> diaries = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Setting setting;

    @OneToMany(mappedBy = "fromUserId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Friend> friends = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Alarm> alarms = new ArrayList<>();

    @Builder
    public User(String name, String nickname, String password, String email) {
        this.name = name;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
    }

    public void addFriend(Friend friend) {
        friends.add(friend);
    }

    public void addAlarm(Alarm alarm) {
        alarms.add(alarm);
    }
}
