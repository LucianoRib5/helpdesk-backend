package com.lucianoribeiro.helpdesk.controller;

import com.lucianoribeiro.helpdesk.dto.*;
import com.lucianoribeiro.helpdesk.model.User;
import com.lucianoribeiro.helpdesk.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

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
    public ResponseEntity<UserBasicInfoDTO> updateUser(@PathVariable Long id, @RequestBody UserRequestDTO dto) {
        UserBasicInfoDTO updatedUser = userService.updateUser(id, dto);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<UserBasicInfoDTO> updateUserStatus(@PathVariable Long id, @RequestBody UpdateUserStatusDTO request) {
        UserBasicInfoDTO updatedUser = userService.updateUserStatus(id, request.getStatus());
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserBasicInfoDTO>> getUserByUserName(@RequestParam String name) {
        List<UserBasicInfoDTO> users = userService.findByUserName(name);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @RequestBody UpdatePasswordDTO request) {
        userService.updatePassword(id, request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/email")
    public ResponseEntity<Void> updateEmail(@PathVariable Long id, @RequestBody UpdateEmailDTO request) {
        userService.updateEmail(id, request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/basic-data")
    public ResponseEntity<UserBasicInfoDTO> updateBasicData(@RequestBody BasicUserDataDTO request) {
        UserBasicInfoDTO updatedUser = userService.updateBasicData(request);
        return ResponseEntity.ok(updatedUser);
    }
}
