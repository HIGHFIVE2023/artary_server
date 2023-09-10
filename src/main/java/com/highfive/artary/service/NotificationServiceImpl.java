package com.highfive.artary.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.highfive.artary.domain.Notification;
import com.highfive.artary.domain.NotificationType;
import com.highfive.artary.domain.User;
import com.highfive.artary.dto.NotificationDto;
import com.highfive.artary.repository.EmitterRepository;
import com.highfive.artary.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final static Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public SseEmitter subscribe(Long userId, String lastEventId) {
        String emitterId = makeTimeIncludeId(userId);
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        // 503에러 방지를 위한 더미 이벤트 전송
        String eventId = makeTimeIncludeId(userId);
        sendNotification(emitter, eventId, emitterId, "EventStream Created. [userId=" + userId + "]");

        if (hasLostData(lastEventId)) {
            sendLostData(lastEventId, userId, emitterId, emitter);
        }

        return emitter;
    }

    private String makeTimeIncludeId(Long userId) {
        return userId + "_" + System.currentTimeMillis();
    }

    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {
        try {
            String jsonData = new ObjectMapper().writeValueAsString(data);
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .name("sse")
                    .data(data)
            );
        } catch (IOException exception) {
            emitterRepository.deleteById(emitterId);
        }
    }

    private boolean hasLostData(String lastEventId) {
        return !lastEventId.isEmpty();
    }

    private void sendLostData(String lastEventId, Long userId, String emitterId, SseEmitter emitter) {
        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByUserId(String.valueOf(userId));
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }

    @Override
    public void send(User user, NotificationType notificationType, String content, String url) {
        Notification notification = notificationRepository.save(createNotification(user, notificationType, content, url));

        String receiverEmail = user.getEmail();
        String userId = new Long(user.getId()).toString();
        String eventId = receiverEmail + "_" + System.currentTimeMillis();
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByUserId(userId);
        emitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, notification);
                    sendNotification(emitter, eventId, key, new NotificationDto(notification));
                }
        );
    }

    private Notification createNotification(User user, NotificationType notificationType, String content, String url) {
        return Notification.builder()
                .user(user)
                .type(notificationType)
                .content(content)
                .url(url)
                .checked(false)
                .build();
    }

    // 알림 리스트
    @Override
    public List<NotificationDto> getNotifications(Long userId) {
        List<NotificationDto> notificationDtos = new ArrayList<>();

        List<Notification> notifications = notificationRepository.findAllByUserIdAndChecked(userId, false);

        for (Notification notification : notifications) {
            NotificationDto notificationDto = new NotificationDto(notification);
            notificationDtos.add(notificationDto);
        }

        return notificationDtos;
    }

    // 알림 읽음
    @Override
    public void checkNotification(Long alarmId) {
        Notification notification = notificationRepository.findById(alarmId).orElseThrow(() ->
                new IllegalArgumentException("해당 알림이 존재하지 않습니다."));

        notification.setChecked(true);
        notificationRepository.save(notification);
    }

    // 알림 삭제
    @Override
    public void deleteNotification(Long alarmId) {
        Notification notification = notificationRepository.findById(alarmId).orElseThrow(() ->
                new IllegalArgumentException("해당 알림이 존재하지 않습니다."));

        notificationRepository.delete(notification);
    }
}
