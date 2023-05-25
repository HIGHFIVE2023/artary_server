package com.highfive.artary.config.auth;

import com.highfive.artary.domain.User;
import lombok.Getter;

import java.io.Serializable;
@Getter
public class SessionUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String email;
    private String image;
    private String nickname;

    public SessionUser(User user){
        this.name = user.getName();
        this.email = user.getEmail();
        this.image = user.getImage();
        this.nickname = user.getNickname();

    }
}
