package com.lucianoribeiro.helpdesk.repository;

import com.lucianoribeiro.helpdesk.model.TicketUpdateHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketUpdateHistoryRepository extends JpaRepository<TicketUpdateHistory, Long> {

    List<TicketUpdateHistory> findByTicketIdOrderByUpdatedAtDesc(Long ticketId);
}
