package com.example.lms.repository;

import com.example.lms.model.Mentee;
import com.example.lms.model.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TutorRepository extends JpaRepository<Tutor, Long> {
    Tutor findTutorById(Long tutorId);
}
