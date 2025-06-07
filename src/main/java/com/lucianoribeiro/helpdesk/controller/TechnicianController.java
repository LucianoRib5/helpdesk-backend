package com.lucianoribeiro.helpdesk.controller;

import com.lucianoribeiro.helpdesk.dto.TechnicianDTO;
import com.lucianoribeiro.helpdesk.model.Technician;
import com.lucianoribeiro.helpdesk.service.TechnicianService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/technicians")
@RequiredArgsConstructor
public class TechnicianController {

    private final TechnicianService technicianService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<TechnicianDTO> getTechnicianByUserId(@PathVariable Long userId) {
        Technician technician = technicianService.findByUserId(userId);
        return ResponseEntity.ok().body(TechnicianDTO.from(technician));
    }

    @GetMapping
    public ResponseEntity<List<TechnicianDTO>> getAllAvailableTechnicians() {
        List<TechnicianDTO> technicianDTOs = technicianService.findAvailableTechnician()
                .stream()
                .map(TechnicianDTO::from)
                .toList();

        return ResponseEntity.ok(technicianDTOs);
    }
}
