package com.lucianoribeiro.helpdesk.controller;

import com.lucianoribeiro.helpdesk.dto.CloseTicketDTO;
import com.lucianoribeiro.helpdesk.dto.CommetDTO;
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
    public ResponseEntity<Page<TicketResponseDTO>> getTicketsByCustomerId(
            @PathVariable Long customerId,
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

        Page<TicketResponseDTO> result = ticketService.getTicketsByCustomerId(customerId, title, status, priority, pageable);
        return ResponseEntity.ok(result);
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
    public ResponseEntity<TicketResponseDTO> getTicketById(@PathVariable Long ticketId) {
        TicketResponseDTO ticket = ticketService.getTicketById(ticketId);
        return new ResponseEntity<>(ticket, HttpStatus.OK);
    }

    @PutMapping("/{ticketId}/close")
    public ResponseEntity<TicketResponseDTO> closeTicket(
            @PathVariable Long ticketId,
            @RequestBody CloseTicketDTO dto
    ) {
        TicketResponseDTO closedTicket = ticketService.closeTicket(ticketId, dto);
        return new ResponseEntity<>(closedTicket, HttpStatus.OK);
    }

    @PostMapping("/{ticketId}/add-comment")
    public ResponseEntity<Void> addComment(
            @PathVariable Long ticketId,
            @RequestBody CommetDTO commentDTO
    ) {
        ticketService.addComment(ticketId, commentDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
