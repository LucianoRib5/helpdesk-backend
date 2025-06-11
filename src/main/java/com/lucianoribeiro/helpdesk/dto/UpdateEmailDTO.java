package com.lucianoribeiro.helpdesk.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateEmailDTO {

    private String newEmail;
    private String password;
}
