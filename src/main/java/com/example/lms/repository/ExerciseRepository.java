package com.example.lms.repository;

import com.example.lms.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    @Query(value = """
        SELECT e.* 
        FROM exercises e
        JOIN lessons l ON e.lesson_id = l.id
        JOIN courses c ON l.course_id = c.course_id
        JOIN subject_registrations sr ON c.registration_id = sr.id
        WHERE sr.tutor_id = :tutorId
        """, nativeQuery = true)
    List<Exercise> findAllByTutorIdNative(@Param("tutorId") Long tutorId);
}
