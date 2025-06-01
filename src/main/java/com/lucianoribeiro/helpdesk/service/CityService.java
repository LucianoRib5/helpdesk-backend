package com.lucianoribeiro.helpdesk.service;

import com.lucianoribeiro.helpdesk.model.City;
import com.lucianoribeiro.helpdesk.repository.CityRepository;
import com.lucianoribeiro.helpdesk.service.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;

    public List<City> findAll() {
        return cityRepository.findAll();
    }

    public City getByCep(String cep) {
        Optional<City> city = cityRepository.findByCep(cep);
        return city.orElseThrow(() -> new ObjectNotFoundException("Infelizmente, ainda não atendemos essa região."));
    }
}
