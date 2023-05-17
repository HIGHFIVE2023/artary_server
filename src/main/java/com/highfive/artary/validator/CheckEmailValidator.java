package com.highfive.artary.validator;

import com.highfive.artary.dto.UserDto;
import com.highfive.artary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@RequiredArgsConstructor
@Component
public class CheckEmailValidator extends AbstractValidator<UserDto> {

    private final UserRepository userRepository;

    @Override
    protected void doValidate(UserDto dto, Errors errors) {
        if(userRepository.existsByEmail(dto.getEmail())){
            errors.rejectValue("email", "이메일 중복 오류", "이미 사용 중인 이메일입니다.");
        }

    }
}
