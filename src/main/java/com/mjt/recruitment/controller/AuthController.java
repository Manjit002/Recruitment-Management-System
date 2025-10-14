package com.mjt.recruitment.controller;

import com.mjt.recruitment.dto.LoginRequest;
import com.mjt.recruitment.dto.SignupRequest;
import com.mjt.recruitment.entity.User;
import com.mjt.recruitment.entity.UserType;
import com.mjt.recruitment.repository.UserRepository;
import com.mjt.recruitment.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signup")
    @Operation(summary = "Create a profile", description = "Create a user account (Applicant or Admin)")
    public ResponseEntity<?> signup(@RequestBody SignupRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email already taken"));
        }
        User user = new User();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setAddress(req.getAddress());
        user.setProfileHeadline(req.getProfileHeadline());
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        user.setUserType(UserType.valueOf(req.getUserType()));
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "user created"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail()).orElse(null);
        if (user == null || !passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            return ResponseEntity.status(401).body(Map.of("error", "invalid credentials"));
        }
        String token = jwtUtil.generateToken(user.getEmail(), user.getUserType().name());
        return ResponseEntity.ok(Map.of("token", token));
    }
}
