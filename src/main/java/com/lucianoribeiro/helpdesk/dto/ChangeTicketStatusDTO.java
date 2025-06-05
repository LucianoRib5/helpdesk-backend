package com.lucianoribeiro.helpdesk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangeTicketStatusDTO {

    private Integer statusId;
    private Long updatedById;
}
