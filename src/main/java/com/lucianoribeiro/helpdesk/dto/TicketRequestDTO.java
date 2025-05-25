package com.lucianoribeiro.helpdesk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketRequestDTO {

    private String title;
    private String description;
    private Integer priorityId;
    private Long customerId;

}
