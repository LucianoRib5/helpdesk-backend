package com.lucianoribeiro.helpdesk.dto;

import com.lucianoribeiro.helpdesk.model.Technician;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class TechnicianDTO {

    private Long id;
    private Long userId;
    private String name;
    private Integer assignedTicketsCount;
    private Integer statusId;
    private LocalTime workShiftStart;
    private LocalTime workShiftEnd;

    public static TechnicianDTO from (Technician technician) {
        TechnicianDTO dto = new TechnicianDTO();
        dto.setId(technician.getId());
        dto.setUserId(technician.getUser().getId());
        dto.setName(technician.getUser().getName());
        dto.setAssignedTicketsCount(technician.getAssignedTicketsCount());
        dto.setStatusId(technician.getStatus().getId());
        dto.setWorkShiftStart(technician.getWorkShiftStart());
        dto.setWorkShiftEnd(technician.getWorkShiftEnd());
        return dto;
    }
}