package com.lucianoribeiro.helpdesk.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordDTO {

    private String newPassword;
    private String currentPassword;
}
