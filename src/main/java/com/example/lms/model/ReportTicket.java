package com.example.lms.model;

import com.example.lms.enums.ReportTicketStatus;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "report_tickets")
@Data
public class ReportTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mentee_id", nullable = false)
    private Mentee mentee;

    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ReportTicketStatus status;
    private String adminResponse;
    private LocalDateTime createdAt;
    private LocalDateTime resolvedAt;
}
