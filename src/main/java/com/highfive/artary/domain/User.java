package com.highfive.artary.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import javax.persistence.*;
import java.util.*;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Data
@ToString(callSuper = true, exclude = {"friends", "notifications"})
@EqualsAndHashCode(callSuper = true)
@Table(name = "user")
public class User extends BaseEntity implements UserDetails{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_name")
    @NonNull
    private String name;

    @NonNull
    @Column(unique = true)
    private String nickname;

    @NonNull
    private String password;

    @NonNull
    private String auth;

    @NonNull
    @Email
    @Column(unique = true)
    private String email;

    @Column(name = "profile_img", length = 65535, columnDefinition="TEXT")
    private String image;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Diary> diaries = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TemporaryDiary> temporarydiaries = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "fromUserId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Friend> friends = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> roles = new HashSet<>();
        for (String role : auth.split(",")) {
            roles.add(new SimpleGrantedAuthority(role));
        }
        return roles;
    }

    @Builder
    public User(Long id, String name, String nickname, String password, String email, String auth, String image) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.auth = auth;
        this.image = image;
    }

    public void addFriend(Friend friend) {
        friends.add(friend);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
