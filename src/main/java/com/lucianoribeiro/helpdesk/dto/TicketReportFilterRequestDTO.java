package com.lucianoribeiro.helpdesk.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class TicketReportFilterRequestDTO {

    private LocalDate startDate;
    private LocalDate endDate;
    private List<Integer> priorities;
    private List<Integer> statuses;
}

