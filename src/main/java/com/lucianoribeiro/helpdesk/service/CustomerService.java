package com.lucianoribeiro.helpdesk.service;

import com.lucianoribeiro.helpdesk.dto.CustomerDTO;
import com.lucianoribeiro.helpdesk.model.Customer;
import com.lucianoribeiro.helpdesk.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public ArrayList<CustomerDTO> findAll() {
        ArrayList<Customer> customers = (ArrayList<Customer>) customerRepository.findAll();
        return customers.stream()
                .map(CustomerDTO::from)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public CustomerDTO findByUserId(Long userId) {
        Customer customer = customerRepository.findByUserId(userId);
        return CustomerDTO.from(customer);
    }
}
