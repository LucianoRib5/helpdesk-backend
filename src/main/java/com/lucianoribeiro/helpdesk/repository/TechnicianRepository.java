package com.lucianoribeiro.helpdesk.repository;

import com.lucianoribeiro.helpdesk.model.Technician;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TechnicianRepository extends JpaRepository<Technician, Long> {}