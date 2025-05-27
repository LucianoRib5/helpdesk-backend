package com.lucianoribeiro.helpdesk.controller;

import com.lucianoribeiro.helpdesk.dto.AuthRequestDTO;
import com.lucianoribeiro.helpdesk.dto.AuthResponseDTO;
import com.lucianoribeiro.helpdesk.dto.UserRequestDTO;
import com.lucianoribeiro.helpdesk.dto.UserResponseDTO;
import com.lucianoribeiro.helpdesk.model.User;
import com.lucianoribeiro.helpdesk.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.net.URI;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody @Valid UserRequestDTO dto) {
        User createdUser = userService.createUser(dto);
        URI location = URI.create("/users/" + createdUser.getId());
        return ResponseEntity.created(location).build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO dto) {
        AuthResponseDTO response = userService.login(dto.getEmail(), dto.getPassword());
        return ResponseEntity.ok(response);
    }
}
