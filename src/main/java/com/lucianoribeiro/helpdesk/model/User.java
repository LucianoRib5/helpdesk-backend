package com.lucianoribeiro.helpdesk.model;

import com.lucianoribeiro.helpdesk.dto.UserRequestDTO;
import com.lucianoribeiro.helpdesk.enums.UserStatusEnum;
import com.lucianoribeiro.helpdesk.enums.UserTypeEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String cpf;
    private String cnpj;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "user_password")
    private String password;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private UserType type;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private UserStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public static User from(
            UserRequestDTO userRequestDTO,
            PasswordEncoder passwordEncoder,
            UserType userType
    ) {
        User user = new User();
        UserStatus defaultStatus = createDefaultStatus();
        UserType defaultType = createDefaultType();


        user.setName(userRequestDTO.getName());
        user.setEmail(userRequestDTO.getEmail());
        user.setCpf(userRequestDTO.getCpf());
        user.setCnpj(userRequestDTO.getCnpj());
        user.setPhoneNumber(userRequestDTO.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        user.setStatus(defaultStatus);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        user.setType(Objects.requireNonNullElse(userType, defaultType));

        return user;
    }

    private static UserStatus createDefaultStatus() {
        UserStatus status = new UserStatus();
        status.setId(UserStatusEnum.ACTIVE.getId());
        status.setDescription(UserStatusEnum.ACTIVE.getDescription());
        return status;
    }

    private static UserType createDefaultType() {
        UserType type = new UserType();
        type.setId(UserTypeEnum.CUSTOMER.getId());
        type.setDescription(UserTypeEnum.CUSTOMER.getDescription());
        return type;
    }

}
