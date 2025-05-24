package com.lucianoribeiro.helpdesk.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

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
}
