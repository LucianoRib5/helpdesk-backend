package com.lucianoribeiro.helpdesk.dto;


import com.lucianoribeiro.helpdesk.model.Customer;
import com.lucianoribeiro.helpdesk.model.User;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomerDTO {

    private Long id;
    private String name;
    private String email;
    private String cpf;
    private String cnpj;

    public static CustomerDTO from (Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        User user = customer.getUser();

        dto.setId(customer.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setCpf(user.getCpf());
        dto.setCnpj(user.getCnpj());
        return dto;
    }
}
