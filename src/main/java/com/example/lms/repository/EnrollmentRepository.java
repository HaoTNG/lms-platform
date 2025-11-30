package com.example.lms.repository;

import com.example.lms.model.Enrollment;
import com.example.lms.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    // Find by course (method naming: findBy<PropertyName>)
    Page<Enrollment> findByCourse(Course course, Pageable pageable);
}
