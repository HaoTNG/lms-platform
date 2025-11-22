package com.example.lms.repository;

import com.example.lms.model.AnnouncementUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementUserRepository extends JpaRepository<AnnouncementUser, Long> {
}
