package com.example.lms.repository;

import com.example.lms.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepository extends JpaRepository<Grade, Long> {}
