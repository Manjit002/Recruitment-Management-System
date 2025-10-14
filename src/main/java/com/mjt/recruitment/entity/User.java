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
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;


    @Column(unique = true, nullable = false)
    private String email;


    private String address;


    private String passwordHash;


    private String profileHeadline;


    @Enumerated(EnumType.STRING)
    private UserType userType; // ADMIN or APPLICANT


    @OneToOne(mappedBy = "applicant", cascade = CascadeType.ALL)
    private Profile profile;
}

