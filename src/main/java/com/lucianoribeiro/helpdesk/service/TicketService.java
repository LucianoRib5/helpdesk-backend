package com.lucianoribeiro.helpdesk.service;

import com.lucianoribeiro.helpdesk.dto.*;
import com.lucianoribeiro.helpdesk.enums.TicketPriorityEnum;
import com.lucianoribeiro.helpdesk.enums.TicketStatusEnum;
import com.lucianoribeiro.helpdesk.enums.UserTypeEnum;
import com.lucianoribeiro.helpdesk.model.*;
import com.lucianoribeiro.helpdesk.repository.*;
import com.lucianoribeiro.helpdesk.service.exception.ObjectNotFoundException;
import com.lucianoribeiro.helpdesk.specifications.TicketSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.lucianoribeiro.helpdesk.enums.TicketStatusEnum.toTicketStatus;
import static com.lucianoribeiro.helpdesk.service.TicketHistoryMessageBuilder.buildHistoryMessage;


@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final TicketUpdateHistoryRepository ticketUpdateHistoryRepository;
    private final TicketHistoryService ticketHistoryService;
    private final TechnicianRepository technicianRepository;

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

    public Page<TicketResponseDTO> getTicketsByUserRoleId(
            Long userRoleId,
            Integer userTypeId,
            String title,
            Integer status,
            Integer priority,
            Pageable pageable
    ) {
        Specification<Ticket> spec = Specification
                .where(TicketSpecifications.hasTitle(title))
                .and(TicketSpecifications.hasStatus(status))
                .and(TicketSpecifications.hasPriority(priority));

        if (userTypeId == UserTypeEnum.CUSTOMER.getId()) {
            spec = spec.and(TicketSpecifications.hasCustomerId(userRoleId));
        } else if (userTypeId == UserTypeEnum.TECHNICIAN.getId()) {
            spec = spec.and(TicketSpecifications.hasTechnicianId(userRoleId));
        }

        return ticketRepository.findAll(spec, pageable)
                .map(TicketResponseDTO::from);
    }

    public Page<TicketResponseDTO> getAllTickets(String title, Integer status, Integer priority, Boolean notAssigned, Pageable pageable) {
        Specification<Ticket> spec = Specification
                .where(TicketSpecifications.hasTitle(title))
                .and(TicketSpecifications.hasStatus(status))
                .and(TicketSpecifications.hasPriority(priority));

        if (notAssigned != null && notAssigned) {
            spec = spec.and(TicketSpecifications.hasNotAssigned());
        }

        return ticketRepository.findAll(spec, pageable)
                .map(TicketResponseDTO::from);
    }

    public TicketResponseDTO getTicketById(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ObjectNotFoundException("Ticket não encontrado com o ID: " + ticketId));

        ArrayList<TicketUpdateDTO> updateHistory = buildUpdateHistory(ticketId);

        return TicketResponseDTO.from(ticket, updateHistory);
    }

    public TicketResponseDTO closeTicket(Long ticketId, CloseTicketDTO dto) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ObjectNotFoundException("Ticket não encontrado com o ID: " + ticketId));

        if (ticket.getTicketStatusId() != TicketStatusEnum.AWAITING_EVALUATION.getId()) {
            throw new IllegalStateException("O ticket não está no status de Aguardando Avaliação");
        }

        ticket.setRating(dto.getRating());
        ticket.setRatingComment(dto.getRatingComment());
        ticket.setStatus(toTicketStatus(TicketStatusEnum.CLOSED));

        Ticket updatedTicket = ticketRepository.save(ticket);

        ticketHistoryService.logChange(
                ticket,
                ticket.getCustomer().getUser(),
                "status change",
                TicketStatusEnum.AWAITING_EVALUATION.getTranslatedDescription(),
                TicketStatusEnum.CLOSED.getTranslatedDescription(),
                null
        );

        ArrayList<TicketUpdateDTO> updateHistory = buildUpdateHistory(ticketId);

        return TicketResponseDTO.from(updatedTicket, updateHistory);
    }

    public void addComment(Long ticketId, CommetDTO comment) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ObjectNotFoundException("Ticket não encontrado com o ID: " + ticketId));

        User user = userRepository.findById(comment.getUserId())
                .orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado com o ID: " + comment.getUserId()));

        ticketHistoryService.logChange(
                ticket,
                user,
                "comment added",
                null,
                null,
                comment.getComment()
        );
    }

    public void changeStatus(Long ticketId, ChangeTicketStatusDTO dto) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ObjectNotFoundException("Ticket não encontrado com o ID: " + ticketId));

        User updatedBy = userRepository.findById(dto.getUpdatedById())
                .orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado com o ID: " + dto.getUpdatedById()));

        final TicketStatus oldStatus = ticket.getStatus();

        TicketStatus newStatus = toTicketStatus(TicketStatusEnum.fromId(dto.getStatusId()));

        ticket.setStatus(newStatus);

        ticketRepository.save(ticket);

        ticketHistoryService.logChange(
                ticket,
                updatedBy,
                "status change",
                TicketStatusEnum.fromId(oldStatus.getId()).getTranslatedDescription(),
                TicketStatusEnum.fromId(newStatus.getId()).getTranslatedDescription(),
                null
        );
    }

    public ArrayList<TicketUpdateDTO> buildUpdateHistory(Long ticketId) {
        List<TicketUpdateHistory> historyList = ticketUpdateHistoryRepository
                .findByTicketIdOrderByUpdatedAtDesc(ticketId);

        ArrayList<TicketUpdateDTO> updateDTOs = new ArrayList<>();

        for (TicketUpdateHistory h : historyList) {
            String message = buildHistoryMessage(h);
            updateDTOs.add(new TicketUpdateDTO(message, h.getUpdatedAt()));
        }

        return updateDTOs;
    }

    public TicketResponseDTO updateTicket(Long ticketId, TicketRequestDTO dto) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ObjectNotFoundException("Ticket não encontrado com o ID: " + ticketId));

        if (dto.getTitle() != null && !dto.getTitle().isEmpty()) {
            ticket.setTitle(dto.getTitle());
        }

        if (dto.getDescription() != null && !dto.getDescription().isEmpty()) {
            ticket.setDescription(dto.getDescription());
        }

        if (dto.getPriorityId() != null) {
            TicketPriority priority = new TicketPriority();
            priority.setTicketPriorityEnum(TicketPriorityEnum.fromId(dto.getPriorityId()));
            ticket.setPriority(priority);
        }

        if (dto.getCustomerId() != null) {
            Customer customer = customerRepository.findById(dto.getCustomerId())
                    .orElseThrow(() -> new ObjectNotFoundException("Cliente não encontrado com o ID: " + dto.getCustomerId()));
            ticket.setCustomer(customer);
        }

        ticket.setUpdatedAt(LocalDateTime.now());

        Ticket updatedTicket = ticketRepository.save(ticket);
        return TicketResponseDTO.from(updatedTicket);
    }

    public void assignMultipleTickets(Long technicianId, AssignMultipleTicketsDTO dto) {
        Technician technician = technicianRepository.findById(technicianId)
                .orElseThrow(() -> new ObjectNotFoundException("Técnico não encontrado com o ID: " + technicianId));

        List<Ticket> tickets = ticketRepository.findAllById(dto.getTicketIds());

        for (Ticket ticket : tickets) {
            ticket.setTechnician(technician);
            ticketRepository.save(ticket);
        }
    }

    public TicketReportResponseDTO generateReport(TicketReportFilterRequestDTO filter) {
        Specification<Ticket> spec = Specification.where((root, query, cb) -> cb.between(
                root.get("createdAt"),
                filter.getStartDate().atStartOfDay(),
                filter.getEndDate().atTime(23, 59, 59)
        ));

        if (filter.getPriorities() != null && !filter.getPriorities().isEmpty()) {
            spec = spec.and((root, query, cb) -> root.get("priority").get("id").in(filter.getPriorities()));
        }

        if (filter.getStatuses() != null && !filter.getStatuses().isEmpty()) {
            spec = spec.and((root, query, cb) -> root.get("status").get("id").in(filter.getStatuses()));
        }

        List<Ticket> tickets = ticketRepository.findAll(spec);

        TicketReportResponseDTO response = new TicketReportResponseDTO();
        response.setTotalTickets(tickets.size());

        response.setTicketsByStatus(
                tickets.stream()
                        .collect(Collectors.groupingBy(t -> TicketStatusEnum.fromId(t.getStatus().getId()).getTranslatedDescription(), Collectors.counting()))
                        .entrySet().stream()
                        .map(e -> new TicketReportResponseDTO.CountDTO(e.getKey(), e.getValue()))
                        .toList()
        );

        response.setTicketsByPriority(
                tickets.stream()
                        .collect(Collectors.groupingBy(t -> TicketPriorityEnum.fromId(t.getTicketPriorityId()).getTranslatedDescription(), Collectors.counting()))
                        .entrySet().stream()
                        .map(e -> new TicketReportResponseDTO.CountDTO(e.getKey(), e.getValue()))
                        .toList()
        );

        response.setTicketsOverTime(
                tickets.stream()
                        .collect(Collectors.groupingBy(t -> t.getCreatedAt().toLocalDate().toString(), Collectors.counting()))
                        .entrySet().stream()
                        .map(e -> new TicketReportResponseDTO.CountByDateDTO(e.getKey(), e.getValue()))
                        .sorted(Comparator.comparing(TicketReportResponseDTO.CountByDateDTO::date))
                        .toList()
        );

        return response;
    }
}
