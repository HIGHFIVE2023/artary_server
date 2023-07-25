package com.highfive.artary.repository;

import com.highfive.artary.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    Boolean existsByNickname(String email);
    Optional<User> findByEmailAndPassword(String email, String password);
    Optional<User> findByNameAndNickname(String name, String nickname);

}
