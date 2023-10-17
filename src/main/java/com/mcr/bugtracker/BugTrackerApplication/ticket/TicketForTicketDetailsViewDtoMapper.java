package com.mcr.bugtracker.BugTrackerApplication.ticket;

import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;

import java.time.temporal.ChronoUnit;
import java.util.function.Function;

public class TicketForTicketDetailsViewDtoMapper implements Function<Ticket, TicketForTicketDetailsViewDto> {
    @Override
    public TicketForTicketDetailsViewDto apply(Ticket ticket) {
        return new TicketForTicketDetailsViewDto(ticket.getTitle(),
                ticket.getDescription(),
                ticket.getPriority(),
                ticket.getStatus(),
                ticket.getType(),
                ticket.getCreatedAt().truncatedTo(ChronoUnit.SECONDS),
                ticket.getOptionalDeveloper().map(AppUser::getWholeName).orElse("Not specified"),
                ticket.getOptionalSubmitter().map(AppUser::getWholeName).orElse("Not specified"),
                ticket.getOptionalSubmitter().map(AppUser::getEmail).orElse("Not specified"),
                ticket.getProject().getId(),
                ticket.getProject().getName(),
                ticket.getProject().getProjectManager().getWholeName());
    }
}
