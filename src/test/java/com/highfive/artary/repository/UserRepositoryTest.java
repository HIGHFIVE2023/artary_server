package com.highfive.artary.repository;

import com.highfive.artary.domain.User;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DiaryRepository diaryRepository;

    @Autowired
    private SettingRepository settingRepository;

    @Test
    @DisplayName("회원을 저장한다.")
    void createUser() {
        User user = User.builder()
                .name("박소연")
                .nickname("소연")
                .email("sy@naver.com")
                .password("thdus")
                .build();

        User savedUser = userRepository.save(user);

        assertThat(savedUser).isEqualTo(user);
    }


}