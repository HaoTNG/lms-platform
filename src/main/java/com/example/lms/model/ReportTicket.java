package com.example.lms.model;

import com.example.lms.enums.ReportTicketStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "report_tickets")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mentee_id", nullable = false)
    private Mentee mentee;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ReportTicketStatus status = ReportTicketStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    private String adminResponse;

    @ManyToOne
    @JoinColumn(name = "resolved_by_admin_id")
    private Admin resolvedBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime resolvedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = ReportTicketStatus.PENDING;
        }
    }
}