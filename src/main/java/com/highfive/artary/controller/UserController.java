package com.highfive.artary.controller;

import com.highfive.artary.domain.User;
import com.highfive.artary.dto.UserDto;
import com.highfive.artary.repository.UserRepository;
import com.highfive.artary.security.TokenProvider;
import com.highfive.artary.service.FriendService;
import com.highfive.artary.service.MailService;
import com.highfive.artary.service.UserService;

import com.highfive.artary.validator.CheckEmailValidator;
import com.highfive.artary.validator.CheckNicknameValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final CheckNicknameValidator checkNicknameValidator;
    private final CheckEmailValidator checkEmailValidator;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final FriendService friendService;

    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity<?> signup(@Valid @RequestBody UserDto userDto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(getErrorMap(bindingResult));
        }
        userService.save(userDto);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/signup/email/{email}/exists")
    public ResponseEntity<Boolean> checkEmailDuplicate(@PathVariable String email) {
        return ResponseEntity.ok(userService.checkEmailDuplication(email));
    }

    @GetMapping("/signup/nickname/{nickname}/exists")
    public ResponseEntity<Boolean> checkNicknameDuplicate(@PathVariable String nickname) {
        return ResponseEntity.ok(userService.checkNicknameDuplication(nickname));
    }

    // 비밀번호 찾기
    @PostMapping("/password")
    @ResponseBody
    public ResponseEntity<?> mailConfirm(@RequestParam String email){
       Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            try {
                String mailResponse = mailService.sendSimpleMessage(email);
                return ResponseEntity.ok().body(mailResponse);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("{\"message\": \"이메일 전송 실패\"}");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"message\": \"가입되지 않은 E-MAIL 입니다.\"}");
        }
    }

    // 이메일 찾기
    @PostMapping("/email")
    public ResponseEntity<String> findEmail(@RequestParam String name, @RequestParam String nickname){
        Optional<String> result = userService.findEmail(name, nickname);
        return result.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // 회원 탈퇴
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long userId, @RequestParam String password) {
        try {
            boolean passwordMatches = userService.checkPassword(userId, password);

            if(!passwordMatches){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"비밀번호가 일치하지 않습니다.\"}");
            }
            friendService.withdrawalFriend(userId);
            userService.deleteById(userId);
            return ResponseEntity.ok().body("{\"message\": \"회원 탈퇴 성공\"}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"message\": \"회원 탈퇴 실패\"}");
        }
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> userDto) {
        String email = userDto.get("email");
        String password = userDto.get("password");

        User member = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", "비밀번호가 일치하지 않습니다."));
        }

        try {
            String token = tokenProvider.createToken(member.getUsername());

            UserDto responseDto = UserDto.builder()
                    .token(token)
                    .userId(member.getId())
                    .name(member.getName())
                    .nickname(member.getNickname())
                    .email(member.getEmail())
                    .auth(member.getAuth())
                    .image(member.getImage())
                    .build();

            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "토큰 생성 중 오류가 발생했습니다."));
        }
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

    // 유저 정보 업데이트
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody UserDto userDto) {
        try {
            userService.updateUser(userId, userDto);
            return ResponseEntity.ok(userDto);
        } catch (ResponseStatusException e) {
            if (e.getStatus() == HttpStatus.NOT_FOUND) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Collections.singletonMap("error", "사용자 정보 수정 실패"));
            }
        }
    }

    // 닉네임으로 회원 정보 찾기
    @GetMapping("/findUser/{nickname}")
    public ResponseEntity<?> findUserIdByNickname(@PathVariable String nickname) {
        Long id = userService.findUserIdByNickname(nickname);

        return ResponseEntity.ok(id);
    }

    // 커스텀 유효성 검증
    @InitBinder
    public void validatorBinder(WebDataBinder binder) {
        binder.addValidators(checkNicknameValidator);
        binder.addValidators(checkEmailValidator);
    }

    private Map<String, String> getErrorMap(BindingResult bindingResult) {
        Map<String, String> errorMap = new HashMap<>();
        for (FieldError error : bindingResult.getFieldErrors()) {
            errorMap.put("valid_" + error.getField(), error.getDefaultMessage());
        }
        return errorMap;
    }
}