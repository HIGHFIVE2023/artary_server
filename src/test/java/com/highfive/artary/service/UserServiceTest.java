package com.highfive.artary.service;

import com.highfive.artary.domain.User;
import com.highfive.artary.service.helper.UserTestHelper;
import com.highfive.artary.service.helper.WithUserTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest extends WithUserTest {

    @BeforeEach
    protected void before(){
        prepareUserServices();
    }

    @DisplayName("사용자 생성")
    @Test
    void createUserTest() {
        userTestHelper.createUser("user1");
        List<User> list = StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        assertEquals(1,list.size());
        UserTestHelper.assertUser(list.get(0), "user1");
    }

    @DisplayName("email 검색")
    @Test
    void searchEmailTest() {
        User user1 = userTestHelper.createUser("user1");
        User savedUser = (User) userService.loadUserByUsername("user1@test.com");
        userTestHelper.assertUser(savedUser, "user1");
    }


}