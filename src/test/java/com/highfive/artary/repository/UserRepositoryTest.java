package com.highfive.artary.repository;

import com.highfive.artary.domain.User;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user, user2, user3;

    @BeforeEach
     void setUp(){

        user = User.builder()
                .name("테스트1")
                .nickname("테1")
                .password("1234")
                .email("test1@gmail.com")
                .build();

        user2 = User.builder()
                .name("테스트2")
                .nickname("테2")
                .password("1234")
                .email("test2@gmail.com")
                .build();

        user3 = User.builder()
                .name("테스트3")
                .nickname("테3")
                .password("1234")
                .email("test3@gmail.com")
                .build();

        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);
    }

    @Test
    @DisplayName("아이디로 유저 찾기")
    void findByIdTest(){
        User find = userRepository.findById(user.getId()).orElse(null);
        assertThat(find).isEqualTo(user);
    }

    @Test
    @DisplayName("이메일로 유저 찾기")
     void findByEmailTest(){
        User find = userRepository.findByEmail(user.getEmail());
        assertThat(find).isEqualTo(user);
    }

    @Test
    @DisplayName("모든 유저 조회")
    void findAllTest(){
        userRepository.findAll().forEach(System.out::println);
    }

    @Test
    @DisplayName("닉네임 업데이트")
    public void updateNickNameTest(){
        User saved = userRepository.save(user);
        saved.setNickname("닉네임변경");
        User changedNicknameUser = userRepository.save(saved);
        assertThat(changedNicknameUser).isEqualTo(saved);
    }

    @Test
    @DisplayName("유저 삭제")
    public void deleteUserTest(){
        userRepository.save(user);
        userRepository.delete(user);
        assertThat(userRepository.findByEmail(user.getEmail())).isNull();
    }

}