package com.lucianoribeiro.helpdesk.model;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "user_types")
@Getter
public class UserType {

    @Id
    private Integer id;

    private String description;

}
