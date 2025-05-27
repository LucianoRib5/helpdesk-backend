package com.lucianoribeiro.helpdesk.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_types")
@Getter
@Setter
public class UserType {

    @Id
    private Integer id;

    private String description;

}
