package com.mjt.recruitment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String title;


    @Column(columnDefinition = "TEXT")
    private String description;


    private LocalDateTime postedOn;


    private int totalApplications;


    private String companyName;


    @ManyToOne
    @JoinColumn(name = "posted_by")
    private User postedBy;


    @ManyToMany
    @JoinTable(name = "job_applicants",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "applicant_id"))
    private Set<User> applicants = new HashSet<>();
}
