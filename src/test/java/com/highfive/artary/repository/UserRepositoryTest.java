package com.highfive.artary.repository;

import com.highfive.artary.domain.User;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("회원을 저장한다.")
    void createUserTest() {
        User user = new User("김덕성", "덕성", "1234", "kim3@duksung.com");
        User savedUser = userRepository.save(user);

        assertThat(savedUser).isEqualTo(user);

        log.info(">>> create " + savedUser);
    }

    @Test
    @DisplayName("사용자를 추가하고 업데이트한다.")
    void insertAndUpdateTest() {
        User user = new User("김덕성", "덕성", "1234", "kim3@duksung.com");
        userRepository.save(user);

        log.info(">>> 수정 전 "+user);

        User user2 = userRepository.findById(1L).orElseThrow(RuntimeException::new);
        user2.setPassword("1345");
        userRepository.save(user2);

        log.info(">>> 수정 후" + user2);
    }

}