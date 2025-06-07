package com.lucianoribeiro.helpdesk.repository;

import com.lucianoribeiro.helpdesk.model.Technician;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Optional;

public interface TechnicianRepository extends JpaRepository<Technician, Long> {

    Optional<Technician> findByUserId(Long userId);
    ArrayList<Technician> findAllByStatusId(Integer statusId);
}