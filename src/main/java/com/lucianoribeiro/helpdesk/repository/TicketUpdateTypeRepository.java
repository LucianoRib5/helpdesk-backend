package com.lucianoribeiro.helpdesk.repository;

import com.lucianoribeiro.helpdesk.model.TicketUpdateType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketUpdateTypeRepository extends JpaRepository<TicketUpdateType, Long> {
    Optional<TicketUpdateType> findByDescription(String description);
}
