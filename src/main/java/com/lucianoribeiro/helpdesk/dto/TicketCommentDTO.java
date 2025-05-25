package com.lucianoribeiro.helpdesk.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TicketCommentDTO {

    private Long id;
    private String userName;
    private LocalDateTime updatedAt;
    private String comment;

}