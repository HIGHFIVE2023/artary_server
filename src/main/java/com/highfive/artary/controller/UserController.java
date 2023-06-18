package com.highfive.artary.controller;

import com.highfive.artary.domain.User;
import com.highfive.artary.dto.UserDto;
import com.highfive.artary.repository.UserRepository;
import com.highfive.artary.security.TokenProvider;
import com.highfive.artary.service.MailService;
import com.highfive.artary.service.UserService;

import com.highfive.artary.validator.CheckEmailValidator;
import com.highfive.artary.validator.CheckNicknameValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;



@RestController
@Slf4j
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final MailService mailService;
    private final CheckNicknameValidator checkNicknameValidator;
    private final CheckEmailValidator checkEmailValidator;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity<?> signup(@Valid @RequestBody UserDto userDto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put("valid_" + error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errorMap);
        }
        userService.save(userDto);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/signup/email/{email}/exists")
    public ResponseEntity<Boolean> checkEmailDuplicate(@PathVariable String email){
        return ResponseEntity.ok(userService.checkEmailDuplication(email));
    }

    @GetMapping("/signup/nickname/{nickname}/exists")
    public ResponseEntity<Boolean> checkNicknameDuplicate(@PathVariable String nickname){
        return ResponseEntity.ok(userService.checkNicknameDuplication(nickname));
    }

    @PostMapping("/password/mailConfirm")
    @ResponseBody
    public String mailConfirm(@RequestParam String email) throws Exception{
        String code = mailService.sendSimpleMessage(email);
        return code;
    }

    // 커스텀 유효성 검증
    @InitBinder
    public void validatorBinder(WebDataBinder binder) {
        binder.addValidators(checkNicknameValidator);
        binder.addValidators(checkEmailValidator);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> userDto) {
        log.info("user email = {}", userDto.get("email"));
        User member = userRepository.findByEmail(userDto.get("email"))
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));

        String token;
        try {
            token = tokenProvider.createToken(member.getUsername());
        } catch (Exception e) {
            // 토큰 생성 중 오류가 발생한 경우
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "토큰 생성 중 오류가 발생했습니다."));
        }

        UserDto responseDto = new UserDto();

        responseDto.setToken(token);
        responseDto.setName(member.getName());
        responseDto.setNickname(member.getNickname());
        responseDto.setPassword(member.getPassword());
        responseDto.setEmail(member.getEmail());
        responseDto.setAuth(member.getAuth());
        responseDto.setImage(member.getImage());

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/login")
    public String getUserInfo(@AuthenticationPrincipal Object principal) {
        return principal.toString();
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return ResponseEntity.ok("로그아웃 성공");
    }

}
