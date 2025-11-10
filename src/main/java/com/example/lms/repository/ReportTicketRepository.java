package com.example.lms.repository;

import com.example.lms.model.ReportTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportTicketRepository extends JpaRepository<ReportTicket, Long> {}
