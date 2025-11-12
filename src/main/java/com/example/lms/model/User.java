package com.example.lms.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Placeholder entity: User
 * Fields and relations to be implemented by the team.
 */
@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String name;
    private String password_hashed;
    private String role;
    private String email;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Admin admin;
}
