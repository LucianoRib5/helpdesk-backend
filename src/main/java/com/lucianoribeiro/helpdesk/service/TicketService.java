package com.lucianoribeiro.helpdesk.service;

import com.lucianoribeiro.helpdesk.dto.CloseTicketDTO;
import com.lucianoribeiro.helpdesk.dto.TicketRequestDTO;
import com.lucianoribeiro.helpdesk.dto.TicketResponseDTO;
import com.lucianoribeiro.helpdesk.enums.TicketPriorityEnum;
import com.lucianoribeiro.helpdesk.enums.TicketStatusEnum;
import com.lucianoribeiro.helpdesk.model.*;
import com.lucianoribeiro.helpdesk.repository.CustomerRepository;
import com.lucianoribeiro.helpdesk.repository.TicketRepository;
import com.lucianoribeiro.helpdesk.repository.UserRepository;
import com.lucianoribeiro.helpdesk.service.exception.ObjectNotFoundException;
import com.lucianoribeiro.helpdesk.specifications.TicketSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    public TicketResponseDTO createTicket(TicketRequestDTO ticketRequestDTO) {
        if (ticketRequestDTO.getCustomerId() == null) {
            throw new IllegalArgumentException("ID do cliente é obrigatório");
        }

        if (ticketRequestDTO.getTitle() == null || ticketRequestDTO.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Título é obrigatório");
        }

        if (ticketRequestDTO.getDescription() == null || ticketRequestDTO.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Descrição é obrigatória");
        }

        Customer customer = customerRepository.findById(ticketRequestDTO.getCustomerId())
                .orElseThrow(() -> new ObjectNotFoundException("Cliente não encontrado"));

        User createdBy = userRepository.findById(ticketRequestDTO.getCreatedById())
                .orElseThrow(() -> new ObjectNotFoundException("Usuário criador não encontrado"));

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
        Optional<Ticket> ticket = ticketRepository.findById(ticketId);
        return ticket.map(TicketResponseDTO::from)
                .orElseThrow(() -> new ObjectNotFoundException("Ticket não encontrado com o ID: " + ticketId));
    }

    public TicketResponseDTO closeTicket(Long ticketId, CloseTicketDTO dto) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ObjectNotFoundException("Ticket não encontrado com o ID: " + ticketId));

        if (ticket.getTicketStatusId() != TicketStatusEnum.AWAITING_EVALUATION.getId()) {
            throw new IllegalStateException("O ticket não está no status de Aguardando Avaliação");
        }

        ticket.setRating(dto.getRating());
        ticket.setRatingComment(dto.getRatingComment());
        ticket.setStatus(TicketStatusEnum.toTicketStatus(TicketStatusEnum.CLOSED));

        Ticket updatedTicket = ticketRepository.save(ticket);
        return TicketResponseDTO.from(updatedTicket);
    }
}
