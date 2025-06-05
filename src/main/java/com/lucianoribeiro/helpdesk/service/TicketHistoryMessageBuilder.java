package com.lucianoribeiro.helpdesk.service;

import com.lucianoribeiro.helpdesk.model.TicketUpdateHistory;


public class TicketHistoryMessageBuilder {

    public static String buildHistoryMessage(TicketUpdateHistory h) {
        switch (h.getUpdateType().getDescription()) {
            case "priority change":
                return "O chamado foi marcado como " + formatPriority(h.getNewValue()) + ".";
            case "status change":
                return "Status alterado de " + h.getOldValue() + " para " + h.getNewValue() + ".";
            case "technician assignment":
                return "Técnico " + h.getNewValue() + " atribuído ao chamado.";
            case "comment added":
                return h.getUpdatedBy().getName() + " adicionou um comentário" + ".";
            default:
                return "Atualização desconhecida.";
        }
    }

    private static String formatPriority(String value) {
        if (value == null) return "prioridade indefinida";
        switch (value) {
            case "1": return "baixa prioridade";
            case "2": return "média prioridade";
            case "3": return "alta prioridade";
            default: return value;
        }
    }
}
