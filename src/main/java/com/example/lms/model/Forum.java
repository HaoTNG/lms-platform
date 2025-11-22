package com.example.lms.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "forums")
@Data
public class Forum {

    @Id
    private Long id; 

    @OneToOne
    @MapsId
    @JoinColumn(name = "session_id")
    private Session session;

    @OneToMany(mappedBy = "forum", cascade = CascadeType.ALL)
    private List<Question> questions;
}