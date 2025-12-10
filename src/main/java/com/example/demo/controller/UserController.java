package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.UpdateProfileRequest;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    // ⭐ GET all managers for dropdown
    @GetMapping("/managers")
    public ResponseEntity<?> getManagers() {
        return ResponseEntity.ok(userService.getAllManagers());
    }

    // ⭐ GET profile (existing details)
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserProfile(@PathVariable Long id) {
        return ResponseEntity.ok(userRepository.findById(id));
    }

    // ⭐ UPDATE profile (including Reporting Manager)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfile(
            @PathVariable Long id,
            @RequestBody UpdateProfileRequest req
    ) {
        User updated = userService.updateProfile(id, req);
        return ResponseEntity.ok(updated);
    }
}
