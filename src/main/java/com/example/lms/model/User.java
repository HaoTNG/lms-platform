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
    private Long User_ID;

    private String Name;
    private String Password_hashed;
    private String Role;
    private String Email;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Admin admin;
}
