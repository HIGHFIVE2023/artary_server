package com.highfive.artary.service;

import com.highfive.artary.domain.Notification;
import com.highfive.artary.domain.NotificationType;
import com.highfive.artary.domain.User;
import com.highfive.artary.dto.NotificationDto;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public interface NotificationService {

    SseEmitter subscribe(Long userId, String lastEventId);
    void send(User user, NotificationType notificationType, String content, String url);
    List<NotificationDto> getNotifications(Long userId);
    void checkNotification(Long alarmId);
    void deleteNotification(Long alarmId);
}
