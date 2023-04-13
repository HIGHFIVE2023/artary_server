package com.highfive.artary.repository;

import com.highfive.artary.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("회원을 저장한다.")
    public void createUser() {
        User user = User.builder()
                .name("박소연")
                .nickname("소연")
                .email("sy@nave.com")
                .password("thdus")
                .build();

        User savedUser = userRepository.save(user);

        assertThat(savedUser).isEqualTo(user);
    }

}