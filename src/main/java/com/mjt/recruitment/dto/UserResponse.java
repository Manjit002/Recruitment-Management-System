package com.mjt.recruitment.dto;

import lombok.Data;

@Data
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private String address;
    private String profileHeadline;
    private String userType;
}
