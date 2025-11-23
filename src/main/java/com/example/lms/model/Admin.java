package com.example.lms.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Placeholder entity: Admin
 * Fields and relations to be implemented by the team.
 */
@Entity
@Table(name = "admins")
@DiscriminatorValue("ADMIN")
@Data
@EqualsAndHashCode(callSuper = true)
public class Admin extends User {
    @OneToMany(mappedBy = "resolvedBy", cascade = CascadeType.ALL)
    private List<ReportTicket> reportTickets;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private List<Announcement> announcements;
}