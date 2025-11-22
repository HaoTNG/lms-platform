package com.example.lms.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Placeholder entity: Mentee
 * Fields and relations to be implemented by the team.
 */
@Entity
@Table(name = "mentees")
@DiscriminatorValue("MENTEE")
@Data
@EqualsAndHashCode(callSuper = true)
public class Mentee extends User{
    @OneToMany(mappedBy = "mentee", cascade = CascadeType.ALL)
    private List<ReportTicket> reportTickets;

    @OneToMany(mappedBy = "mentee", cascade = CascadeType.ALL)
    private List<Enrollment> enrollments;

    @OneToMany(mappedBy = "mentee", cascade = CascadeType.ALL)
    private List<Rating> myRatings;

    @OneToMany(mappedBy = "mentee", cascade = CascadeType.ALL)
    private List<Submission> submissions;

    @OneToMany(mappedBy = "mentee", cascade = CascadeType.ALL)
    private List<Question> myQuestions;

    @OneToMany(mappedBy = "mentee", cascade = CascadeType.ALL)
    private List<Conversation> conversations;
}
