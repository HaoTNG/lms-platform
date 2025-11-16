package com.example.lms.repository;

import com.example.lms.model.Announcement;
import com.example.lms.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    List<Announcement> findByRecipientType(String recipientType);
    List<Announcement> findByAdminId(Long adminId);
    List<Announcement> findByRecipientUser(User recipientUser);
    Page<Announcement> findByAdminId(Long adminId, Pageable pageable);

}
