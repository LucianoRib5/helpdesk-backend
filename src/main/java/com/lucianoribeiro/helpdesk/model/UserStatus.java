package com.lucianoribeiro.helpdesk.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_status")
public class UserStatus {

    @Id
    private Long id;

    private String description;

}