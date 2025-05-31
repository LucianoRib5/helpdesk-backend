package com.lucianoribeiro.helpdesk.service;

import com.lucianoribeiro.helpdesk.dto.TicketRequestDTO;
import com.lucianoribeiro.helpdesk.dto.TicketResponseDTO;
import com.lucianoribeiro.helpdesk.enums.TicketPriorityEnum;
import com.lucianoribeiro.helpdesk.enums.TicketStatusEnum;
import com.lucianoribeiro.helpdesk.model.*;
import com.lucianoribeiro.helpdesk.repository.CustomerRepository;
import com.lucianoribeiro.helpdesk.repository.TicketRepository;
import com.lucianoribeiro.helpdesk.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

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

    public ArrayList<TicketResponseDTO> getTicketsByCustomerId(Long customerId) {
        ArrayList<Ticket> tickets = ticketRepository.findByCustomerId(customerId);
        return tickets.stream()
                .map(TicketResponseDTO::from)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<TicketResponseDTO> getAllTickets() {
        ArrayList<Ticket> tickets = (ArrayList<Ticket>) ticketRepository.findAll();
        return tickets.stream()
                .map(TicketResponseDTO::from)
                .collect(Collectors.toCollection(ArrayList::new));
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
