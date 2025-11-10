package com.example.lms.repository;

import com.example.lms.model.Mentee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenteeRepository extends JpaRepository<Mentee, Long> {}
