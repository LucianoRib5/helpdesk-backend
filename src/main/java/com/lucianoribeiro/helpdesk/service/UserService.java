package com.lucianoribeiro.helpdesk.service;

import com.lucianoribeiro.helpdesk.dto.*;
import com.lucianoribeiro.helpdesk.enums.UserStatusEnum;
import com.lucianoribeiro.helpdesk.enums.UserTypeEnum;
import com.lucianoribeiro.helpdesk.model.*;
import com.lucianoribeiro.helpdesk.repository.*;
import com.lucianoribeiro.helpdesk.config.JwtUtil;
import com.lucianoribeiro.helpdesk.service.exception.ObjectInvalidPasswordException;
import com.lucianoribeiro.helpdesk.service.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserTypeRepository typeRepository;
    private final UserPermissionRepository userPermissionRepository;
    private final PasswordEncoder passwordEncoder;
    private final CityRepository cityRepository;
    private final CustomerRepository customerRepository;
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

    public UserBasicInfoDTO updateUser(Long id, UserRequestDTO dto) {
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

        return UserBasicInfoDTO.from(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCpf(),
                user.getCnpj(),
                user.getPhoneNumber(),
                UserTypeEnum.fromId(user.getType().getId()),
                UserStatusEnum.fromId(user.getStatus().getId()),
                UserPermissionDTO.from(userPermissionRepository.findByUserTypeId(user.getType().getId())),
                customerRepository.findByUserId(user.getId())
                        .map(Customer::getCity)
                        .map(City::getCep)
                        .orElse(null),
                customerRepository.findByUserId(user.getId())
                        .map(Customer::getAddress)
                        .orElse(null)
        );
    }

    public AuthResponseDTO login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException("Usuário não cadastrado."));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new ObjectInvalidPasswordException("Senha inválida");
        }

        String token = JwtUtil.generateToken(user.getEmail());

        UserPermission userPermission = userPermissionRepository.findByUserTypeId(user.getType().getId());

        Optional<Customer> customer = customerRepository.findByUserId(user.getId());

        UserBasicInfoDTO userBasicInfo = UserBasicInfoDTO.from(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCpf(),
                user.getCnpj(),
                user.getPhoneNumber(),
                UserTypeEnum.fromId(user.getType().getId()),
                UserStatusEnum.fromId(user.getStatus().getId()),
                UserPermissionDTO.from(userPermission),
                customer.map(Customer::getCity).map(City::getCep).orElse(null),
                customer.map(Customer::getAddress).orElse(null)
        );

        return AuthResponseDTO.from(token, userBasicInfo);
    }

    public UserBasicInfoDTO updateUserStatus(Long id, String status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado."));

        UserStatusEnum userStatusEnum;
        try {
            userStatusEnum = UserStatusEnum.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Status inválido. Use: ACTIVE ou INACTIVE.");
        }

        UserStatus newStatus = new UserStatus();
        newStatus.setId(userStatusEnum.getId());
        newStatus.setDescription(userStatusEnum.getDescription());
        user.setStatus(newStatus);
        return getUserBasicInfoDTO(user);
    }

    public List<UserBasicInfoDTO> findByUserName(String name) {
        List<User> users = userRepository.findByNameContainingIgnoreCase(name);
        if (users.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum usuário encontrado com o nome: " + name);
        }
        return users.stream()
                .map(user -> UserBasicInfoDTO.from(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getCpf(),
                        user.getCnpj(),
                        user.getPhoneNumber(),
                        UserTypeEnum.fromId(user.getType().getId()),
                        UserStatusEnum.fromId(user.getStatus().getId()),
                        UserPermissionDTO.from(userPermissionRepository.findByUserTypeId(user.getType().getId())),
                        customerRepository.findByUserId(user.getId())
                                .map(Customer::getCity)
                                .map(City::getCep)
                                .orElse(null),
                        customerRepository.findByUserId(user.getId())
                                .map(Customer::getAddress)
                                .orElse(null)
                ))
                .toList();
    }

    public void updatePassword(Long id, UpdatePasswordDTO request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado."));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new ObjectInvalidPasswordException("Senha atual inválida.");
        }

        if (request.getNewPassword() == null || request.getNewPassword().isEmpty()) {
            throw new IllegalArgumentException("Nova senha não pode ser vazia.");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    public void updateEmail(Long id, UpdateEmailDTO request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ObjectInvalidPasswordException("Senha inválida");
        }

        if (request.getNewEmail() == null || request.getNewEmail().isEmpty()) {
            throw new IllegalArgumentException("Novo e-mail não pode ser vazio.");
        }

        if (!request.getNewEmail().equals(user.getEmail()) && userRepository.existsByEmail(request.getNewEmail())) {
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }

        user.setEmail(request.getNewEmail());
        userRepository.save(user);
    }

    public UserBasicInfoDTO updateBasicData(BasicUserDataDTO request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado."));

        if (request.getName() != null && !request.getName().isEmpty()) {
            user.setName(request.getName());
        }

        if (request.getPhoneNumber() != null && !request.getPhoneNumber().isEmpty()) {
            user.setPhoneNumber(request.getPhoneNumber());
        }

        if (user.getType().getId() == UserTypeEnum.CUSTOMER.getId()) {
            Customer customer = customerRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new ObjectNotFoundException("Cliente não encontrado."));

            if (request.getCityId() != null) {
                City city = cityRepository.findById(request.getCityId())
                        .orElseThrow(() -> new ObjectNotFoundException("Cidade não encontrada."));

                customer.setCity(city);
            }

            if (request.getAddress() != null && !request.getAddress().isEmpty()) {
                customer.setAddress(request.getAddress());
            } else {
                throw new IllegalArgumentException("Endereço não pode ser vazio para o cliente.");
            }
        }

        return getUserBasicInfoDTO(user);
    }

    private UserBasicInfoDTO getUserBasicInfoDTO(User user) {
        userRepository.save(user);

        Optional<Customer> customer = customerRepository.findByUserId(user.getId());

        return UserBasicInfoDTO.from(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCpf(),
                user.getCnpj(),
                user.getPhoneNumber(),
                UserTypeEnum.fromId(user.getType().getId()),
                UserStatusEnum.fromId(user.getStatus().getId()),
                UserPermissionDTO.from(userPermissionRepository.findByUserTypeId(user.getType().getId())),
                customer.map(Customer::getCity).map(City::getCep).orElse(null),
                customer.map(Customer::getAddress).orElse(null)
        );
    }
}
