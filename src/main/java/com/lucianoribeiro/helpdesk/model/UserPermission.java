package com.lucianoribeiro.helpdesk.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class UserPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_type_id")
    private UserType userType;

    @Column(name = "can_create_ticket")
    private boolean canCreateTicket;

    @Column(name = "can_edit_ticket")
    private boolean canEditTicket;

    @Column(name = "can_assign_ticket")
    private boolean canAssignTicket;

    @Column(name = "can_close_ticket")
    private boolean canCloseTicket;

    @Column(name = "can_manager_reports")
    private boolean canManagerReports;

    @Column(name = "can_manage_users")
    private boolean canManageUsers;

}
