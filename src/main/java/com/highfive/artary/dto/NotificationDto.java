package com.highfive.artary.dto;

import com.highfive.artary.domain.Notification;
import com.highfive.artary.domain.NotificationType;
import lombok.*;

@RequiredArgsConstructor
@Getter
public class NotificationDto {
    String id;
    String name;
    String content;
    String url;
    NotificationType type;
    String createdAt;

    public NotificationDto(Notification notification) {

        this.id = notification.getId().toString();
        this.name = notification.getUser().getName();
        this.content = notification.getContent();
        this.url = notification.getUrl();
        this.type = notification.getType();
        this.createdAt = notification.getCreatedAt().toString();
    }
}
