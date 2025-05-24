package com.lucianoribeiro.helpdesk.repository;

import com.lucianoribeiro.helpdesk.model.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStatusRepository extends JpaRepository<UserStatus, Long> {}
