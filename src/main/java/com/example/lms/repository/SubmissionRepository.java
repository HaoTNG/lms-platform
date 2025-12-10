package com.example.lms.repository;

import com.example.lms.model.Submission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    @Query("""
    SELECT s.mentee.id, AVG(COALESCE(s.grade,0))
    FROM Submission s
    GROUP BY s.mentee.id
    """)
    List<Object[]> getAverageScoreByMentee();

    @Query("""
    SELECT COUNT(s)
    FROM Submission s
    WHERE s.submittedAt < s.exercise.deadline
    """)
    long countOnTime();

    @Query("""
        SELECT COUNT(s)
        FROM Submission s
        WHERE s.submittedAt > s.exercise.deadline
    """)
    long countLate();

    @Query("SELECT COUNT(s) FROM Submission s")
    long countTotal();

    Page<Submission> findByExerciseId(Long exerciseId, Pageable pageable);


    Optional<Submission> findByMenteeIdAndExerciseId(Long menteeId, Long exerciseId);
}
