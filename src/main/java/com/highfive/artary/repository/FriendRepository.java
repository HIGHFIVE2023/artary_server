package com.highfive.artary.repository;

import com.highfive.artary.domain.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Long> {
}
