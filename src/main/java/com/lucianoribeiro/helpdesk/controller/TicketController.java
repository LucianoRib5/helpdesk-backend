package com.lucianoribeiro.helpdesk.controller;

import com.lucianoribeiro.helpdesk.dto.TicketRequestDTO;
import com.lucianoribeiro.helpdesk.dto.TicketResponseDTO;
import com.lucianoribeiro.helpdesk.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<TicketResponseDTO> createTicket(@RequestBody @Valid TicketRequestDTO dto) {
        TicketResponseDTO createdTicket = ticketService.createTicket(dto);
        return new ResponseEntity<>(createdTicket, HttpStatus.CREATED);
    }

}
