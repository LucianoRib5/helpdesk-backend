package com.lucianoribeiro.helpdesk.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TechnicianStatusEnum {
    AVAILABLE(1, "available"),
    BUSY(2, "busy"),
    ABSENT(3, "absent");

    private final Integer id;
    private final String description;

    public static TechnicianStatusEnum fromId(int id) {
        for (TechnicianStatusEnum status : values()) {
            if (status.id == id) {
                return status;
            }
        }
        throw new IllegalArgumentException("Id do status de técnico inválido: " + id);
    }

    public static TechnicianStatusEnum fromDescription(String description) {
        for (TechnicianStatusEnum status : values()) {
            if (status.description.equalsIgnoreCase(description)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Descrição do status de técnico inválida: " + description);
    }
}
