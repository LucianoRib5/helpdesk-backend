package com.lucianoribeiro.helpdesk.service;

import com.lucianoribeiro.helpdesk.enums.TechnicianStatusEnum;
import com.lucianoribeiro.helpdesk.model.*;
import com.lucianoribeiro.helpdesk.repository.TechnicianRepository;
import com.lucianoribeiro.helpdesk.service.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TechnicianService {

    private final TechnicianRepository technicianRepository;

    public void create(User user) {
        Technician technician = new Technician();
        TechnicianStatus status = createDefaultStatus();

        technician.setUser(user);
        technician.setAssignedTicketsCount(0);
        technician.setStatus(status);
        technician.setWorkShiftStart(LocalTime.of(7, 0));
        technician.setWorkShiftEnd(LocalTime.of(17, 48));
        technicianRepository.save(technician);
    }

    private static TechnicianStatus createDefaultStatus() {
        TechnicianStatus status = new TechnicianStatus();
        status.setId(TechnicianStatusEnum.AVAILABLE.getId());
        status.setDescription(TechnicianStatusEnum.AVAILABLE.getDescription());
        return status;
    }

    public Technician findByUserId(Long userId) {
        Optional<Technician> technician = technicianRepository.findByUserId(userId);
        return technician.orElseThrow(() -> new ObjectNotFoundException("Técnico não encontrado com o ID de usuário: " + userId));
    }

    public ArrayList<Technician> findAvailableTechnician() {
        return technicianRepository.findAllByStatusId(TechnicianStatusEnum.AVAILABLE.getId());
    }
}
