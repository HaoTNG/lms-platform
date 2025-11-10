package com.example.lms.repository;

import com.example.lms.model.SubjectRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRegistrationRepository extends JpaRepository<SubjectRegistration, Long> {}
