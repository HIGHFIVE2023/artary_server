package com.highfive.artary.service;

import com.highfive.artary.domain.User;
import com.highfive.artary.dto.UserDto;
import com.highfive.artary.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException(email));
    }

//    public User create(final User user){
//        if(user == null || user.getEmail() == null){
//            throw new RuntimeException("Invalid args");
//        }
//        final String email = user.getEmail();
//        if(userRepository.existsByEmail(email)){
//            log.warn("{} already exists", email);
//            throw new RuntimeException("Email already exists");
//        }
//        return userRepository.save(user);
//    }

    public Long save(UserDto userDto)throws Exception{

        if (userRepository.findByEmail(userDto.getEmail()).isPresent()){
            throw new Exception("이미 존재하는 이메일입니다.");
        }

        if (!userDto.getPassword().equals(userDto.getCheckedPassword())){
            throw new Exception("비밀번호가 일치하지 않습니다.");
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        userDto.setPassword(encoder.encode(userDto.getPassword()));

        return userRepository.save(User.builder()
                .name(userDto.getName())
                .nickname(userDto.getNickname())
                .email(userDto.getEmail())
                .auth(userDto.getAuth()).image(userDto.getImage())
                .password(userDto.getPassword()).build()).getId();
    }



    public Optional<User> getByCredentials(final String email, final String password){
        return userRepository.findByEmailAndPassword(email, password);
    }

    public User save(User user) throws DataIntegrityViolationException {
        if(user.getId() == null){
            user.setCreatedAt(LocalDateTime.now());
        }
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }



}
