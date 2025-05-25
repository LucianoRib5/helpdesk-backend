package com.lucianoribeiro.helpdesk.dto;

import com.lucianoribeiro.helpdesk.enums.UserTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserBasicInfoDTO {

    private Long userId;
    private String userName;
    private UserTypeEnum userType;
    private UserPermissionDTO userPermission;

    public static UserBasicInfoDTO from(Long userId, String userName, UserTypeEnum userType, UserPermissionDTO userPermission) {
        return new UserBasicInfoDTO(userId, userName, userType, userPermission);
    }

}
