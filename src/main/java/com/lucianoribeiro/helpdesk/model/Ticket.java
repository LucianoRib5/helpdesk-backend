package com.lucianoribeiro.helpdesk.model;

import com.lucianoribeiro.helpdesk.dto.TicketRequestDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "technician_id")
    private User technician;

    private String title;
    private String description;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private TicketStatus status;

    @ManyToOne
    @JoinColumn(name = "priority_id")
    private TicketPriority priority;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    private Integer rating;

    @Column(name = "rating_comment")
    private String ratingComment;

    @OneToMany()
    @JoinColumn(name = "ticket_id")
    private List<TicketUpdateHistory> updateHistory;


    public Integer getTicketStatusId() {
        return status.getId();
    }

    public Integer getTicketPriorityId() {
        return priority.getId();
    }

    public static Ticket from(
            TicketRequestDTO ticketRequestDTO,
            Customer customer,
            User createdBy,
            TicketStatus status,
            TicketPriority priority
    ) {
        Ticket ticket = new Ticket();
        ticket.setCustomer(customer);
        ticket.setTitle(ticketRequestDTO.getTitle());
        ticket.setDescription(ticketRequestDTO.getDescription());
        ticket.setStatus(status);
        ticket.setPriority(priority);
        ticket.setCreatedBy(createdBy.getId());
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setUpdatedAt(LocalDateTime.now());
        return ticket;
    }

}
