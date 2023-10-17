package com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistoryField;

import com.mcr.bugtracker.BugTrackerApplication.ticket.Ticket;
import com.mcr.bugtracker.BugTrackerApplication.ticket.TicketForTicketEditViewDto;

import java.util.function.Function;

public class TicketForTicketEditViewMapper implements Function<Ticket, TicketForTicketEditViewDto> {
    @Override
    public TicketForTicketEditViewDto apply(Ticket ticket) {
        return new TicketForTicketEditViewDto(ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getPriority(),
                ticket.getStatus(),
                ticket.getType(),
                ticket.getProject().getName());
    }
}
