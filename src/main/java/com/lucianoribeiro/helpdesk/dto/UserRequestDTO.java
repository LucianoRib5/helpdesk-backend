package com.lucianoribeiro.helpdesk.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDTO {

    private String name;
    private String email;
    private String cpf;
    private String cnpj;
    private String phoneNumber;
    private String password;
    private Long typeId;
    private String address;
    private Integer cityId;
}
