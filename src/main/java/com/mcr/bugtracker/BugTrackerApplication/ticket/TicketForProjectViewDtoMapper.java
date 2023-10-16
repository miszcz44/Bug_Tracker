package com.mcr.bugtracker.BugTrackerApplication.ticket;

import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;

import java.time.temporal.ChronoUnit;
import java.util.function.Function;

public class TicketForProjectViewDtoMapper implements Function<Ticket, TicketForProjectViewDto> {
    @Override
    public TicketForProjectViewDto apply(Ticket ticket) {
        return new TicketForProjectViewDto(ticket.getId(),
                ticket.getTitle(),
                ticket.getOptionalSubmitter().map(AppUser::getWholeName).orElse("Not specified"),
                ticket.getOptionalDeveloper().map(AppUser::getWholeName).orElse("Not specified"),
                ticket.getStatus(),
                ticket.getCreatedAt().truncatedTo(ChronoUnit.SECONDS));
    }
}
