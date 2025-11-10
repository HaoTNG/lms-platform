package com.example.lms.repository;

import com.example.lms.model.Forum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumRepository extends JpaRepository<Forum, Long> {}
