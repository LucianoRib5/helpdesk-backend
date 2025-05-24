package com.lucianoribeiro.helpdesk.service;

import com.lucianoribeiro.helpdesk.dto.AuthResponseDTO;
import com.lucianoribeiro.helpdesk.dto.UserRequestDTO;
import com.lucianoribeiro.helpdesk.dto.UserResponseDTO;
import com.lucianoribeiro.helpdesk.model.User;
import com.lucianoribeiro.helpdesk.model.UserStatus;
import com.lucianoribeiro.helpdesk.model.UserType;
import com.lucianoribeiro.helpdesk.repository.UserRepository;
import com.lucianoribeiro.helpdesk.repository.UserStatusRepository;
import com.lucianoribeiro.helpdesk.repository.UserTypeRepository;
import com.lucianoribeiro.helpdesk.config.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserTypeRepository typeRepository;
    private final UserStatusRepository statusRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO createUser(UserRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado.");
        }

        if (userRepository.existsByCpf(dto.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado.");
        }

        if (dto.getCnpj() != null && userRepository.existsByCnpj(dto.getCnpj())) {
            throw new IllegalArgumentException("CNPJ já cadastrado.");
        }

        if (userRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
            throw new IllegalArgumentException("Telefone já cadastrado.");
        }

        UserType type = typeRepository.findById(dto.getTypeId())
                .orElseThrow(() -> new RuntimeException("Tipo de usuário não encontrado"));

        UserStatus status = statusRepository.findById(dto.getStatusId())
                .orElseThrow(() -> new RuntimeException("Status de usuário não encontrado"));

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setCpf(dto.getCpf());
        user.setCnpj(dto.getCnpj());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setType(type);
        user.setStatus(status);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User saved = userRepository.save(user);
        return UserResponseDTO.from(saved);
    }

    public AuthResponseDTO login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Senha inválida");
        }

        String token = JwtUtil.generateToken(user.getEmail());
        return new AuthResponseDTO(token);
    }
}
