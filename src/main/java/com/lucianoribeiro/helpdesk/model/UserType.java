package com.lucianoribeiro.helpdesk.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_types")
public class UserType {
    @Id
    private Long id;
    private String description;
}
