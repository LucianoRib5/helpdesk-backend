package com.lucianoribeiro.helpdesk.repository;


import com.lucianoribeiro.helpdesk.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);

    boolean existsByCnpj(String cnpj);

    boolean existsByPhoneNumber(String phoneNumber);
}
