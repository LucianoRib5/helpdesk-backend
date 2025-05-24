package com.lucianoribeiro.helpdesk.service;

import com.lucianoribeiro.helpdesk.dto.TicketRequestDTO;
import com.lucianoribeiro.helpdesk.dto.TicketResponseDTO;
import com.lucianoribeiro.helpdesk.enums.TicketPriorityEnum;
import com.lucianoribeiro.helpdesk.enums.TicketStatusEnum;
import com.lucianoribeiro.helpdesk.model.Customer;
import com.lucianoribeiro.helpdesk.model.Ticket;
import com.lucianoribeiro.helpdesk.model.TicketPriority;
import com.lucianoribeiro.helpdesk.model.TicketStatus;
import com.lucianoribeiro.helpdesk.repository.CustomerRepository;
import com.lucianoribeiro.helpdesk.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final CustomerRepository customerRepository;

    public TicketResponseDTO createTicket(TicketRequestDTO ticketRequestDTO) {
        if (ticketRequestDTO.getCustomerId() == null) {
            throw new IllegalArgumentException("Customer ID is required.");
        }

        if (ticketRequestDTO.getTitle() == null || ticketRequestDTO.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title is required.");
        }

        if (ticketRequestDTO.getDescription() == null || ticketRequestDTO.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Description is required.");
        }

        Customer customer = customerRepository.findById(ticketRequestDTO.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        TicketStatus status = new TicketStatus();
        status.setTicketStatusEnum(TicketStatusEnum.OPEN);

        TicketPriority priority = new TicketPriority();
        priority.setTicketPriorityEnum(TicketPriorityEnum.fromId(ticketRequestDTO.getPriorityId()));

        Ticket ticket = Ticket.from(
                ticketRequestDTO,
                customer,
                status,
                priority
        );

        Ticket savedTicket = ticketRepository.save(ticket);
        return TicketResponseDTO.from(savedTicket);
    }

}
