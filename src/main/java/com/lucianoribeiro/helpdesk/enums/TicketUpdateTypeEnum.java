package com.lucianoribeiro.helpdesk.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TicketUpdateTypeEnum {

    STATUS_CHANGE(1, "status change"),
    PRIORITY_CHANGE(2, "priority change"),
    TECHNICIAN_ASSIGNMENT(3, "technician assignment"),
    COMMENT_ADDED(4, "comment added");

    private final int id;
    private final String description;

    public static TicketUpdateTypeEnum fromId(int id) {
        for (TicketUpdateTypeEnum type : values()) {
            if (type.id == id) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid UpdateType id: " + id);
    }

    public static TicketUpdateTypeEnum fromDescription(String description) {
        for (TicketUpdateTypeEnum type : values()) {
            if (type.description.equalsIgnoreCase(description)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid UpdateType description: " + description);
    }

}
