package com.lucianoribeiro.helpdesk.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserTypeEnum {
    CUSTOMER(1, "customer"),
    SUPPORT_OPERATOR(2, "support operator"),
    TECHNICIAN(3, "technician"),
    ADMINISTRATOR(4, "administrator");

    private final int id;
    private final String description;

    public static UserTypeEnum fromId(int id) {
        for (UserTypeEnum type : values()) {
            if (type.id == id) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid UserType id: " + id);
    }

    public static UserTypeEnum fromDescription(String description) {
        for (UserTypeEnum type : values()) {
            if (type.description.equalsIgnoreCase(description)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid UserType description: " + description);
    }
}
