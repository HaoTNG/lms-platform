package com.example.lms.repository;

import com.example.lms.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    @Query(value = """
    SELECT r.* 
    FROM resources r
    JOIN lessons l ON r.lesson_id = l.id
    JOIN courses c ON l.course_id = c.course_id
    JOIN subject_registrations sr ON c.registration_id = sr.id
    WHERE sr.tutor_id = :tutorId
    """, nativeQuery = true)
    List<Resource> findAllByTutorIdNative(Long tutorId);


}
