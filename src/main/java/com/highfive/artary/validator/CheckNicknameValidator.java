package com.highfive.artary.validator;

import com.highfive.artary.dto.UserDto;
import com.highfive.artary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@RequiredArgsConstructor
@Component
public class CheckNicknameValidator extends AbstractValidator<UserDto> {
    private final UserRepository userRepository;
    @Override
    protected void doValidate(UserDto dto, Errors errors) {
        if(userRepository.existsByNickname(dto.getNickname())){
            errors.rejectValue("nickname","닉네임 중복 오류", "이미 사용중인 닉네임입니다.");
        }
    }
}
