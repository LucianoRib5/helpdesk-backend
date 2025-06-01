package com.lucianoribeiro.helpdesk.controller;

import com.lucianoribeiro.helpdesk.dto.CustomerDTO;
import com.lucianoribeiro.helpdesk.model.Customer;
import com.lucianoribeiro.helpdesk.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customers = customerService.findAll();
        return ResponseEntity.ok().body(customers);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CustomerDTO> getCustomerByUserId(@PathVariable Long userId) {
        Customer customer = customerService.findByUserId(userId);
        return ResponseEntity.ok().body(CustomerDTO.from(customer));
    }
}
