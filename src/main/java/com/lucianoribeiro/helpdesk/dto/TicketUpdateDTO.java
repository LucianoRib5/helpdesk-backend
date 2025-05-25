package com.lucianoribeiro.helpdesk.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TicketUpdateDTO {

    private Long id;
    private String userName;
    private Integer updateTypeId;
    private String oldValue;
    private String newValue;
    private LocalDateTime updateAt;

}
