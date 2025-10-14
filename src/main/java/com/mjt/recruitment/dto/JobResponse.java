package com.mjt.recruitment.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JobResponse {

    private Long id;
    private String title;
    private String description;
    private String companyName;
    private LocalDateTime postedOn;
    private int totalApplications;
}
