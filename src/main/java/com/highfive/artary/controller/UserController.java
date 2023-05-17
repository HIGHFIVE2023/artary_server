package com.highfive.artary.controller;

import com.highfive.artary.dto.UserDto;
import com.highfive.artary.service.MailService;
import com.highfive.artary.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final MailService mailService;


    @PostMapping("/signup")
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto) throws Exception {
        try {
            userService.save(userDto);
            return ResponseEntity.ok("회원가입 성공 email: " + userDto.getEmail());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입 실패");
        }
    }

    @PostMapping("/signup/mailConfirm")
    @ResponseBody
    public String mailConfirm(@RequestParam String email) throws Exception{
        String code = mailService.sendSimpleMessage(email);
        return code;
    }


    //login, logout 재작성해야함


}
