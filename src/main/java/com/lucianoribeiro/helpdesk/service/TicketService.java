package com.lucianoribeiro.helpdesk.service;

import com.lucianoribeiro.helpdesk.dto.TicketRequestDTO;
import com.lucianoribeiro.helpdesk.dto.TicketResponseDTO;
import com.lucianoribeiro.helpdesk.enums.TicketPriorityEnum;
import com.lucianoribeiro.helpdesk.enums.TicketStatusEnum;
import com.lucianoribeiro.helpdesk.model.*;
import com.lucianoribeiro.helpdesk.repository.CustomerRepository;
import com.lucianoribeiro.helpdesk.repository.TicketRepository;
import com.lucianoribeiro.helpdesk.repository.UserRepository;
import com.lucianoribeiro.helpdesk.specifications.TicketSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

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

        User createdBy = userRepository.findById(ticketRequestDTO.getCreatedById())
                .orElseThrow(() -> new RuntimeException("User not found"));

        TicketStatus status = new TicketStatus();
        status.setTicketStatusEnum(TicketStatusEnum.OPEN);

        TicketPriority priority = new TicketPriority();
        priority.setTicketPriorityEnum(TicketPriorityEnum.fromId(ticketRequestDTO.getPriorityId()));

        Ticket ticket = Ticket.from(
                ticketRequestDTO,
                customer,
                createdBy,
                status,
                priority
        );

        Ticket savedTicket = ticketRepository.save(ticket);
        return TicketResponseDTO.from(savedTicket);
    }

    public Page<TicketResponseDTO> getTicketsByCustomerId(
            Long customerId,
            String title,
            Integer status,
            Integer priority,
            Pageable pageable
    ) {
        Specification<Ticket> spec = Specification
                .where(TicketSpecifications.hasCustomerId(customerId))
                .and(TicketSpecifications.hasTitle(title))
                .and(TicketSpecifications.hasStatus(status))
                .and(TicketSpecifications.hasPriority(priority));

        return ticketRepository.findAll(spec, pageable)
                .map(TicketResponseDTO::from);
    }

    public Page<TicketResponseDTO> getAllTickets(String title, Integer status, Integer priority, Pageable pageable) {
        Specification<Ticket> spec = Specification
                .where(TicketSpecifications.hasTitle(title))
                .and(TicketSpecifications.hasStatus(status))
                .and(TicketSpecifications.hasPriority(priority));

        return ticketRepository.findAll(spec, pageable)
                .map(TicketResponseDTO::from);
    }

    public TicketResponseDTO getTicketById(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElse(null);
        if (ticket == null) {
            throw new RuntimeException("Ticket not found");
        }
        return TicketResponseDTO.from(ticket);
    }

}
