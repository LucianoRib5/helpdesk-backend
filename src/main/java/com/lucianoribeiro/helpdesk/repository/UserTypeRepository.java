package com.lucianoribeiro.helpdesk.repository;

import com.lucianoribeiro.helpdesk.model.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTypeRepository extends JpaRepository<UserType, Long> {}