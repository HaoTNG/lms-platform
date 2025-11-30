package com.example.lms.repository;

import com.example.lms.model.Course;
import com.example.lms.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {
    @Query("SELECT COUNT(c) FROM Course c")
    long countTotalCourses();

    @Query("SELECT COUNT(c) FROM Course c WHERE c.courseStatus = 'OPEN'")
    long countActiveCourses();

    @Query("SELECT COUNT(c) FROM Course c WHERE c.courseStatus = 'END'")
    long countFinishedCourses();

    @Query("SELECT SUM(size(c.enrollments)) FROM Course c")
    Long countTotalEnrollments();

    Object findBySubjectRegistration(Optional<Subject> subjectRegistration);
}
