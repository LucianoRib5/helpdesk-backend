package com.lucianoribeiro.helpdesk.model;

import com.lucianoribeiro.helpdesk.enums.TicketStatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "ticket_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class TicketStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String description;

    public void setTicketStatusEnum(TicketStatusEnum statusEnum) {
        this.id = (Integer) statusEnum.getId();
        this.description = statusEnum.getDescription();
    }

}
