package com.highfive.artary.service;

import com.highfive.artary.domain.User;
import com.highfive.artary.dto.UserDto;
import com.highfive.artary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException(email));
    }

    public User save(User user) throws DataIntegrityViolationException {
        if(user.getId() == null){
            user.setCreatedAt(LocalDateTime.now());
        }
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public Long save(UserDto userDto) throws Exception{

       PasswordEncoder encoder = new BCryptPasswordEncoder();

        return userRepository.save(User.builder()
                .name(userDto.getName())
                .nickname(userDto.getNickname())
                .email(userDto.getEmail())
                .auth(userDto.getAuth())
                .image(userDto.getImage())
                .password(encoder.encode(userDto.getPassword())).build()).getId();
    }

    public boolean checkEmailDuplication(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean checkNicknameDuplication(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public Optional<String> findEmail(String name, String nickname){
        Optional<User> result = userRepository.findByNameAndNickname(name, nickname);
        return result.map(User::getEmail);
    }

    public void updateUser(Long userId, UserDto userDto) {
        try {
            PasswordEncoder encoder = new BCryptPasswordEncoder();

            User existingUser = userRepository.findById(userId).orElseThrow();
            existingUser.setNickname(userDto.getNickname());
            existingUser.setImage(userDto.getImage());
            existingUser.setPassword(encoder.encode(userDto.getPassword()));

            userRepository.save(existingUser);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.", e);
        }
    }

    public void updatePassword(Long userId, String newPassword) {
        try {
            PasswordEncoder encoder = new BCryptPasswordEncoder();

            User existingUser = userRepository.findById(userId).orElseThrow();
            existingUser.setPassword(encoder.encode(newPassword));

            userRepository.save(existingUser);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.", e);
        }
    }

    public void deleteById(Long userId){
        userRepository.deleteById(userId);
    }

    public boolean checkPassword(Long userId, String password){
        Optional<User> optionalUser = userRepository.findById(userId);

        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            return encoder.matches(password, user.getPassword());
        }
        return false;
    }

    public Long findUserIdByNickname(String nickname) {
        User user = userRepository.findByNickname(nickname).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        return user.getId();
    }
}