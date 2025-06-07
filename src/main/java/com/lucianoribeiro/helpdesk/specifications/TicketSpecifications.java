package com.lucianoribeiro.helpdesk.specifications;

import org.springframework.data.jpa.domain.Specification;
import com.lucianoribeiro.helpdesk.model.Ticket;

public class TicketSpecifications {

    public static Specification<Ticket> hasTitle(String title) {
        return (root, query, cb) ->
                (title == null || title.isEmpty()) ? null :
                        cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<Ticket> hasStatus(Integer statusId) {
        return (root, query, cb) ->
                (statusId == null) ? null : cb.equal(root.get("status").get("id"), statusId);
    }

    public static Specification<Ticket> hasPriority(Integer priorityId) {
        return (root, query, cb) ->
                (priorityId == null) ? null : cb.equal(root.get("priority").get("id"), priorityId);
    }

    public static Specification<Ticket> hasCustomerId(Long customerId) {
        return (root, query, cb) ->
                (customerId == null) ? null : cb.equal(root.get("customer").get("id"), customerId);
    }

    public static Specification<Ticket> hasTechnicianId(Long technicianId) {
        return (root, query, cb) ->
                (technicianId == null) ? null : cb.equal(root.get("technician").get("id"), technicianId);
    }
}
