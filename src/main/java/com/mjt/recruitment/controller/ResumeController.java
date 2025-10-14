package com.mjt.recruitment.controller;

import com.mjt.recruitment.entity.Profile;
import com.mjt.recruitment.entity.User;
import com.mjt.recruitment.repository.UserRepository;
import com.mjt.recruitment.service.ResumeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
public class ResumeController {
    private final UserRepository userRepository;
    private final ResumeService resumeService;

    public ResumeController(UserRepository userRepository, ResumeService resumeService) {
        this.userRepository = userRepository;
        this.resumeService = resumeService;
    }


    @PostMapping("/uploadResume")
    public ResponseEntity<?> uploadResume(@RequestParam("file") MultipartFile file, Authentication authentication) {
        try {
            String email = authentication.getName();
            User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
            if (user.getUserType() != com.mjt.recruitment.entity.UserType.APPLICANT) {
                return ResponseEntity.status(403).body(Map.of("error", "Only applicants can upload resumes"));
            }
            Profile profile = resumeService.saveAndParseResume(user, file);
            return ResponseEntity.ok(Map.of("message", "uploaded and parsed", "profile", profile));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(Map.of("error", "failed to upload/parse resume", "details", ex.getMessage()));
        }
    }
}