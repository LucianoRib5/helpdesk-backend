package com.lucianoribeiro.helpdesk.service;

import com.lucianoribeiro.helpdesk.model.Ticket;
import com.lucianoribeiro.helpdesk.model.TicketUpdateHistory;
import com.lucianoribeiro.helpdesk.model.TicketUpdateType;
import com.lucianoribeiro.helpdesk.model.User;
import com.lucianoribeiro.helpdesk.repository.TicketUpdateHistoryRepository;
import com.lucianoribeiro.helpdesk.repository.TicketUpdateTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TicketHistoryService {

    private final TicketUpdateHistoryRepository historyRepository;
    private final TicketUpdateTypeRepository updateTypeRepository;

    public void logChange(Ticket ticket, User user, String typeDesc,
                          String oldValue, String newValue, String comment) {
        TicketUpdateType type = updateTypeRepository.findByDescription(typeDesc)
                .orElseThrow(() -> new RuntimeException("Update type not found"));

        TicketUpdateHistory history = new TicketUpdateHistory();
        history.setTicket(ticket);
        history.setUpdatedBy(user);
        history.setUpdateType(type);
        history.setOldValue(oldValue);
        history.setNewValue(newValue);
        history.setComment(comment);
        history.setUpdatedAt(LocalDateTime.now());

        historyRepository.save(history);
    }
}
