package com.example.lms.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type")
@Data
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Column(name = "password_hashed")
    private String password;
    private String email;

    @OneToMany(mappedBy = "recipientUser", cascade = CascadeType.ALL)
    private List<Announcement> announcements;
}