package com.mcr.bugtracker.BugTrackerApplication.ticket;

import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class AllTicketsViewMapper implements Function<Ticket, AllTicketsViewDto> {

    @Override
    public AllTicketsViewDto apply(Ticket ticket) {
        return new AllTicketsViewDto(ticket.getId(),
                ticket.getTitle(),
                ticket.getProject().getName(),
                ticket.getAssignedDeveloper().getWholeName(),
                ticket.getPriority(),
                ticket.getStatus(),
                ticket.getType(),
                ticket.getCreatedAt());
    }
}
