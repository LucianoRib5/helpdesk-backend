package com.lucianoribeiro.helpdesk.service;

import com.lucianoribeiro.helpdesk.dto.*;
import com.lucianoribeiro.helpdesk.enums.UserTypeEnum;
import com.lucianoribeiro.helpdesk.model.City;
import com.lucianoribeiro.helpdesk.model.User;
import com.lucianoribeiro.helpdesk.model.UserPermission;
import com.lucianoribeiro.helpdesk.model.UserType;
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

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserTypeRepository typeRepository;
    private final UserPermissionRepository userPermissionRepository;
    private final PasswordEncoder passwordEncoder;
    private final CityRepository cityRepository;
    private final CustomerService customerService;

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

        UserType userType = null;

        if (dto.getTypeId() != null) {
            userType = typeRepository.findById(dto.getTypeId())
                    .orElseThrow(() -> new ObjectNotFoundException("Tipo de usuário não encontrado"));
        }

        User user = User.from(dto, passwordEncoder, userType);

        City city = cityRepository.findById(dto.getCityId())
                .orElseThrow(() -> new ObjectNotFoundException("Cidade não encontrada"));

        userRepository.save(user);

        if (user.getType().getId() == UserTypeEnum.CUSTOMER.getId()) {
            customerService.create(user, dto.getAddress(), city);
        }

        return user;
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
}
