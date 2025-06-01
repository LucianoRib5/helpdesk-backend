package com.lucianoribeiro.helpdesk.repository;

import com.lucianoribeiro.helpdesk.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByUserId(Long userId);
}
