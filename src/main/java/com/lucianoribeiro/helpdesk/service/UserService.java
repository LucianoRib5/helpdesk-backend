package com.lucianoribeiro.helpdesk.service;

import com.lucianoribeiro.helpdesk.dto.*;
import com.lucianoribeiro.helpdesk.enums.UserStatusEnum;
import com.lucianoribeiro.helpdesk.enums.UserTypeEnum;
import com.lucianoribeiro.helpdesk.model.*;
import com.lucianoribeiro.helpdesk.repository.CityRepository;
import com.lucianoribeiro.helpdesk.repository.UserPermissionRepository;
import com.lucianoribeiro.helpdesk.repository.UserRepository;
import com.lucianoribeiro.helpdesk.repository.UserTypeRepository;
import com.lucianoribeiro.helpdesk.config.JwtUtil;
import com.lucianoribeiro.helpdesk.service.exception.ObjectInvalidPasswordException;
import com.lucianoribeiro.helpdesk.service.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserTypeRepository typeRepository;
    private final UserPermissionRepository userPermissionRepository;
    private final PasswordEncoder passwordEncoder;
    private final CityRepository cityRepository;
    private final CustomerService customerService;
    private final TechnicianService technicianService;

    public User createUser(UserRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("E-mail já cadastrado.");
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

        UserType userType = Optional.ofNullable(dto.getTypeId())
                .map(id -> typeRepository.findById(id)
                        .orElseThrow(() -> new ObjectNotFoundException("Tipo de usuário não encontrado")))
                .orElse(null);

        User user = User.from(dto, passwordEncoder, userType);

        City city = Optional.ofNullable(dto.getCityId())
                .map(id -> cityRepository.findById(id)
                        .orElseThrow(() -> new ObjectNotFoundException("Cidade não encontrada")))
                .orElse(null);

        if (user.getType().getId() == UserTypeEnum.CUSTOMER.getId()) {
            if (city == null) {
                throw new ObjectNotFoundException("Cidade não encontrada para o cliente.");
            }

            if (dto.getAddress() == null || dto.getAddress().isEmpty()) {
                throw new IllegalArgumentException("Endereço não pode ser vazio para o cliente.");
            }
        }

        userRepository.save(user);

        if (user.getType().getId() == UserTypeEnum.CUSTOMER.getId()) {
            customerService.create(user, dto.getAddress(), city);
        }

        if (user.getType().getId() == UserTypeEnum.TECHNICIAN.getId()) {
            technicianService.create(user);
        }

        return user;
    }

    public void updateUser(Long id, UserRequestDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado."));

        if (dto.getName() != null && !dto.getName().isEmpty()) {
            user.setName(dto.getName());
        }

        if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {
            if (!dto.getEmail().equals(user.getEmail()) && userRepository.existsByEmail(dto.getEmail())) {
                throw new IllegalArgumentException("E-mail já cadastrado.");
            }
            user.setEmail(dto.getEmail());
        }

        if (dto.getCpf() != null && !dto.getCpf().isEmpty()) {
            if (!dto.getCpf().equals(user.getCpf()) && userRepository.existsByCpf(dto.getCpf())) {
                throw new IllegalArgumentException("CPF já cadastrado.");
            }
            user.setCpf(dto.getCpf());
        }

        if (dto.getCnpj() != null && !dto.getCnpj().isEmpty()) {
            if (!dto.getCnpj().equals(user.getCnpj()) && userRepository.existsByCnpj(dto.getCnpj())) {
                throw new IllegalArgumentException("CNPJ já cadastrado.");
            }
            user.setCnpj(dto.getCnpj());
        }

        if (dto.getPhoneNumber() != null && !dto.getPhoneNumber().isEmpty()) {
            if (!dto.getPhoneNumber().equals(user.getPhoneNumber()) && userRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
                throw new IllegalArgumentException("Telefone já cadastrado.");
            }
            user.setPhoneNumber(dto.getPhoneNumber());
        }

        if (dto.getTypeId() != null) {
            UserType newUserType = typeRepository.findById(dto.getTypeId())
                    .orElseThrow(() -> new ObjectNotFoundException("Tipo de usuário não encontrado"));
            user.setType(newUserType);
        }

        if(user.getType().getId() == UserTypeEnum.CUSTOMER.getId()) {
            customerService.update(user.getId(), dto.getAddress(), dto.getCityId());
        }

        userRepository.save(user);
    }

    public AuthResponseDTO login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException("Usuário não cadastrado."));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new ObjectInvalidPasswordException("Senha inválida");
        }

        String token = JwtUtil.generateToken(user.getEmail());

        UserPermission userPermission = userPermissionRepository.findByUserTypeId(user.getType().getId());

        UserBasicInfoDTO userBasicInfo = UserBasicInfoDTO.from(
                user.getId(),
                user.getName(),
                UserTypeEnum.fromId(user.getType().getId()),
                UserPermissionDTO.from(userPermission)
        );

        return AuthResponseDTO.from(token, userBasicInfo);
    }

    public void inactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado."));

        UserStatus inactiveStatus = new UserStatus();
        inactiveStatus.setId(UserStatusEnum.INACTIVE.getId());
        inactiveStatus.setDescription(UserStatusEnum.INACTIVE.getDescription());
        user.setStatus(inactiveStatus);
        userRepository.save(user);
    }
}
