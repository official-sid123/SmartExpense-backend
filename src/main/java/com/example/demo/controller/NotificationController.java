package com.example.demo.controller;

import com.example.demo.entity.Notification;
import com.example.demo.repository.NotificationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationRepository notificationRepository;

    public NotificationController(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getNotifications(@PathVariable Long userId) {
        List<Notification> list = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/{id}/read")
    public ResponseEntity<?> markRead(@PathVariable Long id) {
        var opt = notificationRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.badRequest().body("Not found");
        Notification n = opt.get();
        n.setReadFlag(true);
        notificationRepository.save(n);
        return ResponseEntity.ok(Map.of("message", "ok"));
    }
}
