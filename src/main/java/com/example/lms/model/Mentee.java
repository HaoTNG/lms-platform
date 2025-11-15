package com.example.lms.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

/**
 * Placeholder entity: Mentee
 * Fields and relations to be implemented by the team.
 */
@Entity
@Data
public class Mentee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "mentee", cascade = CascadeType.ALL)
    private List<ReportTicket> reportTickets;
}
