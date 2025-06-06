package com.lucianoribeiro.helpdesk.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_status")
@Getter
@Setter
public class UserStatus {

    @Id
    private Integer id;

    private String description;

}