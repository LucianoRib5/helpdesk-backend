package com.lucianoribeiro.helpdesk.controller;

import com.lucianoribeiro.helpdesk.dto.CustomerDTO;
import com.lucianoribeiro.helpdesk.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<ArrayList<CustomerDTO>> getAllCustomers() {
        ArrayList<CustomerDTO> customers = customerService.findAll();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CustomerDTO> getCustomerByUserId(@PathVariable("userId") Long userId) {
        CustomerDTO customer = customerService.findByUserId(userId);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }
}
