package com.highfive.artary.controller;

import com.highfive.artary.domain.User;
import com.highfive.artary.dto.UserDto;
import com.highfive.artary.service.UserSecurityService;
import com.highfive.artary.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserSecurityService userSecurityService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody UserDto userDto) throws Exception {
        userSecurityService.save(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //login, logout 재작성해야함

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDto userDto) {
        try {
            return ResponseEntity.ok("로그인에 성공했습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인에 실패했습니다.");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response){
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return ResponseEntity.ok().build();

    }

}
