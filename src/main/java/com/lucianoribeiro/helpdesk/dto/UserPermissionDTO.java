package com.lucianoribeiro.helpdesk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserPermissionDTO {

    private boolean canCreateTicket;
    private boolean canEditTicket;
    private boolean canAssignTicket;
    private boolean canCloseTicket;
    private boolean canManagerReports;
    private boolean canManageUsers;

    public static UserPermissionDTO from(boolean canCreateTicket, boolean canEditTicket, boolean canAssignTicket,
                                          boolean canCloseTicket, boolean canManagerReports, boolean canManageUsers) {
        return new UserPermissionDTO(canCreateTicket, canEditTicket, canAssignTicket, canCloseTicket, canManagerReports, canManageUsers);
    }

}
