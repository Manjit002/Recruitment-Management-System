package com.mjt.recruitment.controller;

import com.mjt.recruitment.entity.Job;
import com.mjt.recruitment.entity.User;
import com.mjt.recruitment.repository.JobRepository;
import com.mjt.recruitment.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jobs")
public class JobController {
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public JobController(JobRepository jobRepository, UserRepository userRepository) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    /**
     * GET /jobs - list job openings (authenticated)
     */
    @GetMapping
    public ResponseEntity<?> listJobs() {
        List<Job> jobs = jobRepository.findAll();
        return ResponseEntity.ok(jobs);
    }

    /**
     * GET /jobs/apply?job_id={job_id} - apply to a job (Applicant only)
     */
    @GetMapping("/apply")
    public ResponseEntity<?> apply(@RequestParam("job_id") Long jobId, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        if (user.getUserType() != com.mjt.recruitment.entity.UserType.APPLICANT) {
            return ResponseEntity.status(403).body(Map.of("error", "Only applicants can apply"));
        }

        Job job = jobRepository.findById(jobId).orElseThrow();
        if (job.getApplicants().contains(user)) {
            return ResponseEntity.badRequest().body(Map.of("error", "already applied"));
        }

        job.getApplicants().add(user);
        job.setTotalApplications(job.getTotalApplications() + 1);
        jobRepository.save(job);

        return ResponseEntity.ok(Map.of("message", "applied"));
    }
}
