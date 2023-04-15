package com.highfive.artary.repository;

import com.highfive.artary.domain.Setting;
import com.highfive.artary.domain.User;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Log4j2
@SpringBootTest
class SettingRepositoryTest {

    @Autowired
    private SettingRepository settingRepository;

    @Test
    @DisplayName("설정 저장")
    void saveSettingTest() {

        User user = new User("김덕성", "덕성", "1234", "kim@duksung.com");
        Setting setting = new Setting(user, "#DC143C", "backimg_url", "#DC143C", "coverimg_url");
        setting.setUser(user);

        Setting savedSetting = settingRepository.save(setting);
        assertThat(savedSetting).isEqualTo(setting);

        log.info(">>> SAVE " + savedSetting);
    }

    @Test
    @DisplayName("설정 업데이트")
    void updateSettingTest() {

        User user = new User("김덕성", "덕성", "1234", "kim2@duksung.com");
        Setting setting = new Setting(user, "#DC143C", "backimg_url", "#DC143C", "coverimg_url");
        Setting savedSetting = settingRepository.save(setting);

        savedSetting.setBackColor("#000000");
        savedSetting.setCoverColor("#FFFFFF");
        settingRepository.save(savedSetting);

        log.info(">>> UPDATE " + savedSetting);
    }

}