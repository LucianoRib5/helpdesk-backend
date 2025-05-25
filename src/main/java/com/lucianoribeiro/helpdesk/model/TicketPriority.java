package com.lucianoribeiro.helpdesk.model;

import com.lucianoribeiro.helpdesk.enums.TicketPriorityEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "ticket_priorities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class TicketPriority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String description;

    public void setTicketPriorityEnum(TicketPriorityEnum priorityEnum) {
        this.id = (Integer) priorityEnum.getId();
        this.description = priorityEnum.getDescription();
    }

}
