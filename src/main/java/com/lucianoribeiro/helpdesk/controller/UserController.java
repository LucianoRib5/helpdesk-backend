package com.lucianoribeiro.helpdesk.controller;

import com.lucianoribeiro.helpdesk.dto.AuthRequestDTO;
import com.lucianoribeiro.helpdesk.dto.AuthResponseDTO;
import com.lucianoribeiro.helpdesk.dto.UserRequestDTO;
import com.lucianoribeiro.helpdesk.model.User;
import com.lucianoribeiro.helpdesk.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO dto) {
        AuthResponseDTO response = userService.login(dto.getEmail(), dto.getPassword());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody UserRequestDTO dto) {
        User createdUser = userService.createUser(dto);
        URI location = URI.create("/users/" + createdUser.getId());
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Long id, @RequestBody UserRequestDTO dto) {
        userService.updateUser(id, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inactivateUser(@PathVariable Long id) {
        userService.inactivateUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<User> getUserByUserName(@RequestParam String name) {
        User user = userService.findByUserName(name);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @RequestBody String newPassword) {
        userService.updatePassword(id, newPassword);
        return ResponseEntity.noContent().build();
    }
}
