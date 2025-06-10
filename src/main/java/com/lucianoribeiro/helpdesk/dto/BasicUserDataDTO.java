package com.lucianoribeiro.helpdesk.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasicUserDataDTO {

    private Long userId;
    private String name;
    private String phoneNumber;
    private Integer cityId;
    private String address;
}
