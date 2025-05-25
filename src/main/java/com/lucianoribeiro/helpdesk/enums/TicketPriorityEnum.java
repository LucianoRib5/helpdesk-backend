package com.lucianoribeiro.helpdesk.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TicketPriorityEnum {

    LOW(1, "low"),
    MEDIUM(2, "medium"),
    HIGH(3, "high");

    private final int id;
    private final String description;

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
