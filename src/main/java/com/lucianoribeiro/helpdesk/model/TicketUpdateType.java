package com.lucianoribeiro.helpdesk.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ticket_update_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class TicketUpdateType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String description;

}
