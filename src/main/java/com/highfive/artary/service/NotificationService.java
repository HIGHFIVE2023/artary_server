package com.highfive.artary.service;

import com.highfive.artary.domain.NotificationType;
import com.highfive.artary.domain.User;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {

    SseEmitter subscribe(Long userId, String lastEventId);
    void send(User user, NotificationType notificationType, String content, String url);
}
