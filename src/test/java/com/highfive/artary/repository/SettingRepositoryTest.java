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
    void settingSaveTest(){

        User user = new User("김덕성", "덕성", "1234", "kim@duksung.com");

        Setting setting = new Setting();
        setting.setUser(user);
        setting.setBackColor("#DC143C");
        setting.setBackImage("backimg_url");
        setting.setCoverColor("#DC143C");
        setting.setCoverImage("coverimg_url");

        Setting savedSetting = settingRepository.save(setting);

        assertThat(savedSetting).isEqualTo(setting);

        log.info(">>> "+ savedSetting);
    }

}