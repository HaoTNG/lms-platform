package com.example.lms.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "conversations", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"mentee_id", "tutor_id"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mentee_id", nullable = false)
    private Mentee mentee;

    @ManyToOne
    @JoinColumn(name = "tutor_id", nullable = false)
    private Tutor tutor;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL)
    private List<Message> messages;

    private LocalDateTime lastMessageAt;

    @PrePersist
    protected void onCreate() {
        this.lastMessageAt = LocalDateTime.now();
    }
}