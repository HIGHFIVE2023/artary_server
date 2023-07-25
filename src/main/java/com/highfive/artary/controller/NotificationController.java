package com.highfive.artary.controller;

import com.highfive.artary.dto.NotificationDto;
import com.highfive.artary.repository.UserRepository;
import com.highfive.artary.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private final NotificationService notificationService;
    @Autowired
    private final UserRepository userRepository;

    @GetMapping(value = "/subscribe/{email}", produces = "text/event-stream")
    public SseEmitter subscribe(@PathVariable String email, @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        Long userId = findIdByEmail(email);

        return notificationService.subscribe(userId, lastEventId);
    }

    // 알림 목록
    @GetMapping
    public ResponseEntity<?> getNotifications(@AuthenticationPrincipal String email) {
        Long userId = findIdByEmail(email);
        List<NotificationDto> notificationDtoList = notificationService.getNotifications(userId);

        return new ResponseEntity<>(notificationDtoList, HttpStatus.OK);
    }

    // 알림 읽음
    @PostMapping("/{alarmId}")
    public ResponseEntity checkAlarm(@PathVariable Long alarmId) {
        notificationService.checkNotification(alarmId);

        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/{alarmId}")
    public ResponseEntity deleteAlarm(@PathVariable Long alarmId) {
        notificationService.deleteNotification(alarmId);

        return ResponseEntity.ok().body("{\"message\": \"삭제 성공\"}");
    }

    private Long findIdByEmail(String email) {
        Long userId = userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자가 존재하지 않습니다.")).getId();

        return userId;
    }
}
