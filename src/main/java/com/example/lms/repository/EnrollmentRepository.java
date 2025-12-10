package com.example.lms.repository;

import com.example.lms.model.Enrollment;
import com.example.lms.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    // Find by course (method naming: findBy<PropertyName>)
    Page<Enrollment> findByCourse(Course course, Pageable pageable);

    List<Enrollment> findByCourse_CourseId(Long courseId);
    Optional<Enrollment> findByMentee_IdAndCourse_CourseId(Long menteeId, Long courseId);


}
