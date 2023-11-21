package com.mcr.bugtracker.BugTrackerApplication.ticket.Mapper;

import com.mcr.bugtracker.BugTrackerApplication.ticket.DTO.TicketForTicketEditViewDto;
import com.mcr.bugtracker.BugTrackerApplication.ticket.Ticket;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
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
