package com.example.lms.repository;

import com.example.lms.model.Mentee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MenteeRepository extends JpaRepository<Mentee, Long> {
    Mentee findMenteeById(long l);
}
