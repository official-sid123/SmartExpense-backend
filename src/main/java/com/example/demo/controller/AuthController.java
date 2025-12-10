package com.example.demo.controller;

import com.example.demo.dto.JwtResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.EmailService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    public AuthController(AuthenticationManager authManager, UserRepository userRepo,
                          PasswordEncoder passwordEncoder, JwtUtil jwtUtil, EmailService emailService) {
        this.authManager = authManager;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {

        if (userRepo.existsByEmail(req.getEmail())) {
            return ResponseEntity.badRequest().body("Email already taken");
        }

        if (req.getRole() == null || req.getRole().isEmpty()) {
            return ResponseEntity.badRequest().body("Role is required");
        }

        User u = new User();
        u.setName(req.getName());
        u.setEmail(req.getEmail());
        u.setPassword(passwordEncoder.encode(req.getPassword()));

        u.setRole("ROLE_" + req.getRole().toUpperCase()); // Perfect
        u.setCreatedAt(LocalDateTime.now());

        userRepo.save(u);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        try {
            Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
            );
            org.springframework.security.core.userdetails.User principal =
                    (org.springframework.security.core.userdetails.User) auth.getPrincipal();

            // load user from DB for extra claims
            User user = userRepo.findByEmail(principal.getUsername()).get();

            HashMap<String, Object> claims = new HashMap<>();
            claims.put("userId", user.getId());
            claims.put("role", user.getRole());
//            claims.put("orgId", user.getOrgId());

            String token = jwtUtil.generateToken(claims, user.getEmail());

            return ResponseEntity.ok(new JwtResponse(token, user.getId(), user.getRole(), user.getOrgId()));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}
