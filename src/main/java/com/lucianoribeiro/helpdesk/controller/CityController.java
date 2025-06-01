package com.lucianoribeiro.helpdesk.controller;

import com.lucianoribeiro.helpdesk.model.City;
import com.lucianoribeiro.helpdesk.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;

    @GetMapping
    public ResponseEntity<List<City>> getAllCities() {
        List<City> cities = cityService.findAll();
        return ResponseEntity.ok().body(cities);
    }

    @GetMapping("/cep/{cep}")
    public ResponseEntity<City> getCityByCep(@PathVariable String cep) {
        City city = cityService.getByCep(cep);
        return ResponseEntity.ok().body(city);
    }
}
