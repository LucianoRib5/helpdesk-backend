package com.lucianoribeiro.helpdesk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDTO {

    private String token;
    private UserBasicInfoDTO user;

    public static AuthResponseDTO from(String token, UserBasicInfoDTO user) {
        return new AuthResponseDTO(token, user);
    }

}
