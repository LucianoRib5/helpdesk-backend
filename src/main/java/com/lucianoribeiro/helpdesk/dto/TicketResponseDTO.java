package com.lucianoribeiro.helpdesk.dto;

import com.lucianoribeiro.helpdesk.enums.TicketUpdateTypeEnum;
import com.lucianoribeiro.helpdesk.model.Ticket;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

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
    private ArrayList<TicketCommentDTO> comments;
    private ArrayList<TicketUpdateDTO> updateHistory;

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
                ticket.getRatingComment(),
                ticket.getUpdateHistory() != null ? ticket.getUpdateHistory().stream()
                        .filter(update -> update.getUpdateType().getId() == TicketUpdateTypeEnum.COMMENT_ADDED.getId())
                        .map(update -> {
                            TicketCommentDTO dto = new TicketCommentDTO();
                            dto.setId(update.getId());
                            dto.setUserName(update.getUpdatedBy() != null ? update.getUpdatedBy().getName() : null);
                            dto.setUpdatedAt(update.getUpdatedAt());
                            dto.setComment(update.getComment());
                            return dto;
                        })
                        .collect(Collectors.toCollection(ArrayList::new)) : new ArrayList<>(),
                ticket.getUpdateHistory() != null ? ticket.getUpdateHistory().stream()
                        .filter(update -> update.getUpdateType().getId() != TicketUpdateTypeEnum.COMMENT_ADDED.getId())
                        .map(update -> {
                            TicketUpdateDTO dto = new TicketUpdateDTO();
                            dto.setId(update.getId());
                            dto.setUserName(update.getUpdatedBy() != null ? update.getUpdatedBy().getName() : null);
                            dto.setUpdateTypeId(update.getUpdateType().getId());
                            dto.setOldValue(update.getOldValue());
                            dto.setNewValue(update.getNewValue());
                            dto.setUpdateAt(update.getUpdatedAt());
                            return dto;
                        })
                        .collect(Collectors.toCollection(ArrayList::new)) : new ArrayList<>()
        );
    }

}
