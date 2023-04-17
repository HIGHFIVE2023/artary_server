package com.highfive.artary.repository;

import com.highfive.artary.domain.Setting;
import com.highfive.artary.domain.User;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Log4j2
@SpringBootTest
@Transactional
class SettingRepositoryTest {
    @Autowired
    private SettingRepository settingRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp(){
        user = User.builder()
                .name("테스트1")
                .nickname("테1")
                .password("1234")
                .email("test1@gmail.com")
                .build();
        userRepository.save(user);

        Setting setting = Setting.builder()
                .user(user)
                .backColor("#FFFFFF")
                .backImage("back.jpg")
                .coverColor("#000000")
                .coverImage("cover.jpg")
                .build();
        settingRepository.save(setting);
    }

    @Test
    @DisplayName("유저 설정 조회")
    void findSettingTest() {
        Setting expected = settingRepository.findByUserId(user.getId());
        assertThat(expected.getUser()).isEqualTo(user);
        assertThat(expected.getBackColor()).isEqualTo("#FFFFFF");
        assertThat(expected.getBackImage()).isEqualTo("back.jpg");
        assertThat(expected.getCoverColor()).isEqualTo("#000000");
        assertThat(expected.getCoverImage()).isEqualTo("cover.jpg");
    }

    @Test
    @DisplayName("유저 설정 업데이트")
    void updateSettingTest() {
        Setting updatedSetting = settingRepository.findByUserId(user.getId());
        updatedSetting.setBackColor("#000000");
        updatedSetting.setBackImage("newback.jpg");
        updatedSetting.setCoverColor("#FFFFFF");
        updatedSetting.setCoverImage("newcover.jpg");

        settingRepository.save(updatedSetting);

        Setting result = settingRepository.findByUserId(user.getId());
        assertThat(result.getBackColor()).isEqualTo("#000000");
        assertThat(result.getBackImage()).isEqualTo("newback.jpg");
        assertThat(result.getCoverColor()).isEqualTo("#FFFFFF");
        assertThat(result.getCoverImage()).isEqualTo("newcover.jpg");
    }
}



