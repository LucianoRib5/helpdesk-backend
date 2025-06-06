package com.lucianoribeiro.helpdesk.repository;

import com.lucianoribeiro.helpdesk.model.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Integer> {

    Optional<City> findByCep(String cep);
}