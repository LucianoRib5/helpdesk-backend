package com.lucianoribeiro.helpdesk.dto;

import com.lucianoribeiro.helpdesk.model.Ticket;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponseDTO {

    private Long id;
    private Long CustomerId;
    private Long TechnicianId;
    private String title;
    private String description;
    private Integer statusId;
    private Integer priorityId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer rating;
    private String ratingComment;

    public static TicketResponseDTO from(Ticket ticket) {
        return new TicketResponseDTO(
                ticket.getId(),
                ticket.getCustomer() != null ? ticket.getCustomer().getId() : null,
                ticket.getTechnician() != null ? ticket.getTechnician().getId() : null,
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getTicketStatusId(),
                ticket.getTicketPriorityId(),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt(),
                ticket.getRating(),
                ticket.getRatingComment()
        );
    }

}
