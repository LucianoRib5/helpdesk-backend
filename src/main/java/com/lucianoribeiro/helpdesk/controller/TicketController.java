package com.lucianoribeiro.helpdesk.controller;

import com.lucianoribeiro.helpdesk.dto.TicketRequestDTO;
import com.lucianoribeiro.helpdesk.dto.TicketResponseDTO;
import com.lucianoribeiro.helpdesk.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

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

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ArrayList<TicketResponseDTO>> getTicketsByCustomerId(@PathVariable("customerId") Long customerId) {
        ArrayList<TicketResponseDTO> tickets = ticketService.getTicketsByCustomerId(customerId);
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ArrayList<TicketResponseDTO>> getAllTickets() {
        ArrayList<TicketResponseDTO> tickets = ticketService.getAllTickets();
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

}
