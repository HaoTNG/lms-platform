package com.example.lms.model;

import com.example.lms.enums.ResourceType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "resources")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    private String title;

    @Column(name = "file_url")
    private String fileUrl;

    @Enumerated(EnumType.STRING)
    private ResourceType resourceType;
}
