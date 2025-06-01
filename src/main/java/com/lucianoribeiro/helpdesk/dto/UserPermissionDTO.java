package com.lucianoribeiro.helpdesk.dto;

import com.lucianoribeiro.helpdesk.model.UserPermission;
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

    public static UserPermissionDTO from(UserPermission userPermission) {
        return new UserPermissionDTO(
                userPermission.isCanCreateTicket(),
                userPermission.isCanEditTicket(),
                userPermission.isCanAssignTicket(),
                userPermission.isCanCloseTicket(),
                userPermission.isCanManagerReports(),
                userPermission.isCanManageUsers()
        );
    }

}
