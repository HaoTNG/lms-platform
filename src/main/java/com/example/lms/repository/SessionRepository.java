package com.example.lms.repository;

import com.example.lms.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    @Query("""
    SELECT s
    FROM Session s
    JOIN s.course c
    JOIN Enrollment e ON e.course = c
    WHERE e.mentee.id = :menteeId
      AND s.date > CURRENT_DATE
    ORDER BY s.date ASC, s.startTime ASC
""")
    List<Session> findUpcomingSessionsForMentee(Long menteeId);

}
