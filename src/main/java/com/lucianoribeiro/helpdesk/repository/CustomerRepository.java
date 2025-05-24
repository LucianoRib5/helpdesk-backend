package com.lucianoribeiro.helpdesk.repository;

import com.lucianoribeiro.helpdesk.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
