package com.example.lms.repository;

import com.example.lms.model.ReportTicket;
import com.example.lms.enums.ReportTicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReportTicketRepository extends JpaRepository<ReportTicket, Long> {
    List<ReportTicket> findByStatus(String status);
    List<ReportTicket> findByStatus(ReportTicketStatus status);
    List<ReportTicket> findByMenteeId(Long menteeId);
}
