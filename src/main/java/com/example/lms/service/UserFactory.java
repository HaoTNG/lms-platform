package com.example.lms.service;

import com.example.lms.model.User;
import com.example.lms.model.Admin;
import com.example.lms.model.Mentee;
import com.example.lms.model.Tutor;

public class UserFactory {
    
    public static User createUser(String role) {
        if (role == null) {
            return new Mentee();
        }
        
        return switch (role.toUpperCase()) {
            case "ADMIN" -> new Admin();
            case "TUTOR" -> new Tutor();
            case "MENTEE" -> new Mentee();
            default -> new Mentee(); // Default is Mentee
        };
    }
}
