package com.example.lms.repository;

import com.example.lms.model.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RateRepository extends JpaRepository<Rating, Long> {
	Page<Rating> findBySessionId(Long sessionId, Pageable pageable);
	Optional<Rating> findById(Long id);
}
