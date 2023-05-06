package com.highfive.artary.service.helper;

import com.highfive.artary.repository.UserRepository;
import com.highfive.artary.service.UserSecurityService;
import com.highfive.artary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

public class WithUserTest {

    @Autowired
    protected UserRepository userRepository;

    protected UserService userService;

    protected UserSecurityService userSecurityService;

    protected UserTestHelper userTestHelper;

    private boolean prepared;

    protected void prepareUserServices (){
        if(prepared) return;
        prepared = true;

        this.userRepository.deleteAll();
        this.userService = new UserService(userRepository);
        this.userSecurityService = new UserSecurityService(userRepository);
        this.userTestHelper = new UserTestHelper(userService, NoOpPasswordEncoder.getInstance());

    }



}
