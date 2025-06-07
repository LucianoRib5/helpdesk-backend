package com.lucianoribeiro.helpdesk.service;

import com.lucianoribeiro.helpdesk.dto.CustomerDTO;
import com.lucianoribeiro.helpdesk.model.City;
import com.lucianoribeiro.helpdesk.model.Customer;
import com.lucianoribeiro.helpdesk.model.User;
import com.lucianoribeiro.helpdesk.repository.CityRepository;
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
    private final CityRepository cityRepository;

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

    public void create(User user, String address, City city) {
        Customer customer = new Customer();
        customer.setUser(user);
        customer.setAddress(address);
        customer.setCity(city);
        customerRepository.save(customer);
    }

    public void update(Long userId, String address, Integer cityId) {
        Customer customer = findByUserId(userId);

        if (address != null && !address.isEmpty()) {
            customer.setAddress(address);
        }

        if (cityId != null && cityId > 0) {
            City city = cityRepository.findById(cityId)
                    .orElseThrow(() -> new ObjectNotFoundException("Cidade não encontrada com o ID: " + cityId));

            customer.setCity(city);
        }

        customerRepository.save(customer);
    }
}
