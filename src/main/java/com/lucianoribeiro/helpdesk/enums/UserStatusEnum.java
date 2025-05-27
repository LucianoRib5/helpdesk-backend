package com.lucianoribeiro.helpdesk.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserStatusEnum {
    ACTIVE(1, "active"),
    INACTIVE(2, "inactive"),
    SUSPENDED(3, "suspended");

    private final int id;
    private final String description;

    public static UserStatusEnum fromId(int id) {
        for (UserStatusEnum status : values()) {
            if (status.id == id) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid UserStatus id: " + id);
    }

    public static UserStatusEnum fromDescription(String description) {
        for (UserStatusEnum status : values()) {
            if (status.description.equalsIgnoreCase(description)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid UserStatus description: " + description);
    }

}