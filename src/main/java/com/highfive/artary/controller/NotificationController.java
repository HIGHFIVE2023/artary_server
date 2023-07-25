package com.highfive.artary.controller;

import com.highfive.artary.repository.UserRepository;
import com.highfive.artary.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

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
        Long userId = userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자가 존재하지 않습니다.")).getId();

        return notificationService.subscribe(userId, lastEventId);
    }
}
