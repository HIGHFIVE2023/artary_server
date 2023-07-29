package com.highfive.artary.service.helper;

import com.highfive.artary.repository.UserRepository;
import com.highfive.artary.service.FriendService;
import com.highfive.artary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class WithUserTest {

    @Autowired
    protected UserRepository userRepository;

    protected UserService userService;

    protected UserTestHelper userTestHelper;

    private boolean prepared;

    protected void prepareUserServices (){
        if(prepared) return;
        prepared = true;

        this.userRepository.deleteAll();
        this.userService = new UserService(userRepository);
        this.userTestHelper = new UserTestHelper(userService, NoOpPasswordEncoder.getInstance());

    }

}
