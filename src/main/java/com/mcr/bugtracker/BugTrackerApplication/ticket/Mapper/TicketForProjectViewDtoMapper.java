package com.mcr.bugtracker.BugTrackerApplication.ticket.Mapper;

import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import com.mcr.bugtracker.BugTrackerApplication.ticket.DTO.TicketForProjectViewDto;
import com.mcr.bugtracker.BugTrackerApplication.ticket.Ticket;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.function.Function;

@Service
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
