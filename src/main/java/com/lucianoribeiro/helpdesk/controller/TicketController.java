package com.lucianoribeiro.helpdesk.controller;

import com.lucianoribeiro.helpdesk.dto.TicketRequestDTO;
import com.lucianoribeiro.helpdesk.dto.TicketResponseDTO;
import com.lucianoribeiro.helpdesk.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public ResponseEntity<Page<TicketResponseDTO>> getAllTickets(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer priority,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<TicketResponseDTO> result = ticketService.getAllTickets(title, status, priority, pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketResponseDTO> getTicketById(@PathVariable("ticketId") Long ticketId) {
        TicketResponseDTO ticket = ticketService.getTicketById(ticketId);
        return new ResponseEntity<>(ticket, HttpStatus.OK);
    }

}
