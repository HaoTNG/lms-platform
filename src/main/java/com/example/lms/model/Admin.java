package com.example.lms.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

/**
 * Placeholder entity: Admin
 * Fields and relations to be implemented by the team.
 */
@Entity
@Data
public class Admin {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name="userId")
    private User user;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
    private List<Announcement> announcements;
}
