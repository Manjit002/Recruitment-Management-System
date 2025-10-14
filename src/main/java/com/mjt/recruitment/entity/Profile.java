package com.mjt.recruitment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User applicant;


    private String resumeFilePath;


    @Column(columnDefinition = "TEXT")
    private String skills;


    @Column(columnDefinition = "TEXT")
    private String education;


    @Column(columnDefinition = "TEXT")
    private String experience;


    private String phone;


    private String extractedName;


    private String extractedEmail;
}
