package com.example.lms.repository;


import com.example.lms.model.Conversation;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
	Optional<Conversation> findByMenteeIdAndTutorId(Long menteeId, Long tutorId);
}
