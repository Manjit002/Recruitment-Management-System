package com.mjt.recruitment.controller;


import com.mjt.recruitment.dto.JobRequest;
import com.mjt.recruitment.entity.Job;
import com.mjt.recruitment.entity.User;
import com.mjt.recruitment.repository.JobRepository;
import com.mjt.recruitment.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/admin")
public class AdminController {
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public AdminController(JobRepository jobRepository, UserRepository userRepository) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    /**
     * POST /admin/job - create job (Admin only)
     */
    @PostMapping("/job")
    public ResponseEntity<?> createJob(@RequestBody JobRequest req, Authentication authentication) {
        String email = authentication.getName();
        User admin = userRepository.findByEmail(email).orElseThrow();
        Job job = new Job();
        job.setTitle(req.getTitle());
        job.setDescription(req.getDescription());
        job.setCompanyName(req.getCompanyName());
        job.setPostedOn(LocalDateTime.now());
        job.setPostedBy(admin);
        job.setTotalApplications(0);
        jobRepository.save(job);
        return ResponseEntity.ok(Map.of("message", "job created", "job", job));
    }

    /**
     * GET /admin/job/{job_id} - job details + applicant list
     */
    @GetMapping("/job/{jobId}")
    public ResponseEntity<?> getJob(@PathVariable Long jobId) {
        Job job = jobRepository.findById(jobId).orElseThrow();
        List<Map<String, ? extends Serializable>> applicants = job.getApplicants()
                .stream()
                .map(u -> Map.of("id", u.getId(), "name", u.getName(), "email", u.getEmail()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(Map.of("job", job, "applicants", applicants));
    }

    /**
     * GET /admin/applicants - list all applicants
     */
    @GetMapping("/applicants")
    public ResponseEntity<?> allApplicants() {
        List<User> all = userRepository.findAll();
        List<User> applicants = all.stream()
                .filter(u -> u.getUserType() == com.mjt.recruitment.entity.UserType.APPLICANT)
                .collect(Collectors.toList());
        return ResponseEntity.ok(applicants);
    }

    /**
     * GET /admin/applicant/{applicant_id} - get extracted data of an applicant
     */
    @GetMapping("/applicant/{applicantId}")
    public ResponseEntity<?> applicantProfile(@PathVariable Long applicantId) {
        User user = userRepository.findById(applicantId).orElseThrow();
        return ResponseEntity.ok(user.getProfile());
    }
}
