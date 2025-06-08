package com.lucianoribeiro.helpdesk.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TicketPriorityEnum {

    LOW(1, "low", "Baixa"),
    MEDIUM(2, "medium", "Média"),
    HIGH(3, "high", "Alta"),;

    private final int id;
    private final String description;
    private final String translatedDescription;

    public static TicketPriorityEnum fromId(int id) {
        for (TicketPriorityEnum priority : values()) {
            if (priority.id == id) {
                return priority;
            }
        }
        throw new IllegalArgumentException("Invalid TicketPriority id: " + id);
    }

    public static TicketPriorityEnum fromDescription(String description) {
        for (TicketPriorityEnum priority : values()) {
            if (priority.description.equalsIgnoreCase(description)) {
                return priority;
            }
        }
        throw new IllegalArgumentException("Invalid TicketPriority description: " + description);
    }

}
