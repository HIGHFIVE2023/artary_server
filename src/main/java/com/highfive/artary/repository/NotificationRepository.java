package com.highfive.artary.repository;

import com.highfive.artary.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByUserIdAndChecked(Long userId, Boolean checked);
    List<Notification> findByUserId(Long userId);
}
