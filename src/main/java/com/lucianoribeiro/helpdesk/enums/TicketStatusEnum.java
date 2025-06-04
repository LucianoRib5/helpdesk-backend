package com.lucianoribeiro.helpdesk.enums;

import com.lucianoribeiro.helpdesk.model.TicketStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TicketStatusEnum {
    OPEN(1, "open", "aberto"),
    IN_PROGRESS(2, "in progress", "em andamento"),
    AWAITING_EVALUATION(3, "awaiting evaluation", "aguardando avaliação"),
    CLOSED(4, "closed", "fechado");

    private final int id;
    private final String description;
    private final String translatedDescription;

    public static TicketStatusEnum fromId(int id) {
        for (TicketStatusEnum status : values()) {
            if (status.id == id) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid TicketStatus id: " + id);
    }

    public static TicketStatusEnum fromDescription(String description) {
        for (TicketStatusEnum status : values()) {
            if (status.description.equalsIgnoreCase(description)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid TicketStatus description: " + description);
    }

    public static TicketStatus toTicketStatus(TicketStatusEnum statusEnum) {
        TicketStatus ticketStatus = new TicketStatus();
        ticketStatus.setId(statusEnum.getId());
        ticketStatus.setDescription(statusEnum.getDescription());
        return ticketStatus;
    }
}

