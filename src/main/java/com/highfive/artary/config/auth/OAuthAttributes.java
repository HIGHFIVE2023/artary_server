package com.highfive.artary.config.auth;

import java.util.Map;

import com.highfive.artary.domain.User;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String image;
    private String nickname;
    private String password;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String image, String nickname, String password) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.image = image;
        this.nickname = nickname;
        this.password = password;

    }

    public static OAuthAttributes of(String socialName, String userNameAttributeName, Map<String, Object> attributes){

        if("kakao".equals(socialName)){
            return ofKakao("id", attributes);
        }
        return null;
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>)kakaoAccount.get("profile");

        return OAuthAttributes.builder()
                .name((String) kakaoProfile.get("nickname"))
                .email((String) kakaoAccount.get("email"))
                .nameAttributeKey(userNameAttributeName)
                .attributes(attributes)
                .build();
    }

    public User toEntity(){
        return User.builder()
                .name(name)
                .nickname(nickname)
                .email(email)
                .image(image)
                .auth("ROLE_USER")
                .build();
    }
}