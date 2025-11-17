package com.example.lms.security;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtUserDetails {
    private String userId;
    private String email;
    private String role;
}
