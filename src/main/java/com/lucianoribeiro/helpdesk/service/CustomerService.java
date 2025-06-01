package com.lucianoribeiro.helpdesk.service;

import com.lucianoribeiro.helpdesk.dto.CustomerDTO;
import com.lucianoribeiro.helpdesk.model.Customer;
import com.lucianoribeiro.helpdesk.repository.CustomerRepository;
import com.lucianoribeiro.helpdesk.service.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public List<CustomerDTO> findAll() {
        return customerRepository.findAll()
                .stream()
                .map(CustomerDTO::from)
                .toList();
    }

    public Customer findByUserId(Long userId) {
        Optional<Customer> customer = customerRepository.findByUserId(userId);
        return customer.orElseThrow(() -> new ObjectNotFoundException("Cliente não encontrado com o ID de usuário: " + userId));
    }
}
