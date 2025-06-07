package com.lucianoribeiro.helpdesk.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AssignMultipleTicketsDTO {

    private List<Long> ticketIds;
}
