package com.lucianoribeiro.helpdesk.dto;

import com.lucianoribeiro.helpdesk.enums.UserStatusEnum;
import com.lucianoribeiro.helpdesk.enums.UserTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@Getter
@AllArgsConstructor
public class UserBasicInfoDTO {

    private Long userId;
    private String userName;
    private String email;
    private String cpf;
    private String cnpj;
    private String phoneNumber;
    private UserTypeEnum userType;
    private Integer userTypeId;
    private UserStatusEnum status;
    private UserPermissionDTO userPermission;
    private String cep;
    private String address;

    public static UserBasicInfoDTO from(
            Long userId,
            String userName,
            String email,
            String cpf,
            String cnpj,
            String phoneNumber,
            UserTypeEnum userType,
            UserStatusEnum status,
            UserPermissionDTO userPermission,
            String cep,
            String address
    ) {
        return new UserBasicInfoDTO(
                userId,
                userName,
                email,
                cpf,
                cnpj,
                phoneNumber,
                Optional.ofNullable(userType).orElse(UserTypeEnum.CUSTOMER),
                Optional.ofNullable(userType).map(UserTypeEnum::getId).orElse(null),
                Optional.ofNullable(status).orElse(UserStatusEnum.ACTIVE), userPermission,
                cep,
                address
        );
    }
}
