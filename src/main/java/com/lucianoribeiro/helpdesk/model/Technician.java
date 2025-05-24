package com.lucianoribeiro.helpdesk.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.time.LocalTime;

@Entity
@Table(name = "technicians")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Technician {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "assigned_tickets_count")
    private Integer assignedTicketsCount;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private TechnicianStatus status;

    @Column(name = "work_shift_start")
    private LocalTime workShiftStart;

    @Column(name = "work_shift_end")
    private LocalTime workShiftEnd;

}
