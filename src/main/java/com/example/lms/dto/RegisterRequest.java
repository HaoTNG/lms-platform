package com.example.lms.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password_hashed;
    private String name;
    private String role;
    private Long userId;
}
