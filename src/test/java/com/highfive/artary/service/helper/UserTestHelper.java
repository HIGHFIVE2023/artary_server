package com.highfive.artary.service.helper;

import com.highfive.artary.domain.User;
import com.highfive.artary.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
public class UserTestHelper {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    public static User makeUser(String name){
        return User.builder()
                .name(name)
                .nickname("test"+name)
                .email(name+"@test.com")
                .auth("ROLE_USER")
                .image("image_url")
                .build();
    }

    public User createUser(String name){
        User user = makeUser(name);
        user.setPassword(passwordEncoder.encode(name+"1234"));
        return userService.save(user);
    }


    public static void assertUser(User user, String name){
        assertNotNull((user.getId()));
        assertNotNull((user.getCreatedAt()));
        assertNotNull((user.getUpdatedAt()));
        assertNotNull((user.getImage()));
        assertTrue(user.isEnabled());
        assertEquals(name, user.getName());
        assertEquals(name+"@test.com", user.getEmail());
    }

}
