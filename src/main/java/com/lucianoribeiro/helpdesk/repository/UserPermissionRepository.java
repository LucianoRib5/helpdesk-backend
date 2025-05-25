package com.lucianoribeiro.helpdesk.repository;

import com.lucianoribeiro.helpdesk.model.UserPermission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPermissionRepository extends JpaRepository<UserPermission, Long> {

    UserPermission findByUserTypeId(Integer userTypeId);

}
