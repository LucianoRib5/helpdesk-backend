package com.lucianoribeiro.helpdesk.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TicketReportResponseDTO {

    private long totalTickets;
    private List<CountDTO> ticketsByStatus;
    private List<CountDTO> ticketsByPriority;
    private List<CountByDateDTO> ticketsOverTime;

    public static record CountDTO(String label, long count) {}
    public static record CountByDateDTO(String date, long count) {}
}
