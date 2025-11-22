package com.example.lms.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

/**
 * Placeholder entity: Tutor
 * Fields and relations to be implemented by the team.
 */
@Entity
@Table(name = "tutors")
@DiscriminatorValue ("TUTOR")
@Data
@EqualsAndHashCode(callSuper = true)
public class Tutor extends User{
    private String introduction;
    private String experience;
    private String expertise;

    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL)
    private List<SubjectRegistration> subjectRegistrations;

    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL)
    private List<Conversation> conversations;
}
