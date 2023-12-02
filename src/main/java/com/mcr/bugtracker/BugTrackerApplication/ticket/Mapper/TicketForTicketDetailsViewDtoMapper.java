package com.mcr.bugtracker.BugTrackerApplication.ticket.Mapper;

import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import com.mcr.bugtracker.BugTrackerApplication.ticket.DTO.TicketForTicketDetailsViewDto;
import com.mcr.bugtracker.BugTrackerApplication.ticket.Ticket;
import com.mcr.bugtracker.BugTrackerApplication.util.DateAndTimeFormatter;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class TicketForTicketDetailsViewDtoMapper implements Function<Ticket, TicketForTicketDetailsViewDto> {
    @Override
    public TicketForTicketDetailsViewDto apply(Ticket ticket) {
        return new TicketForTicketDetailsViewDto(ticket.getTitle(),
                ticket.getDescription(),
                ticket.getPriority(),
                ticket.getStatus(),
                ticket.getType(),
                DateAndTimeFormatter.convertDateAndTimeToCurrentUserZone(ticket.getCreatedAt()),
                ticket.getOptionalDeveloper().map(AppUser::getWholeName).orElse("Not specified"),
                ticket.getOptionalSubmitter().map(AppUser::getWholeName).orElse("Not specified"),
                ticket.getOptionalSubmitter().map(AppUser::getEmail).orElse("Not specified"),
                ticket.getProject().getId(),
                ticket.getProject().getName(),
                ticket.getProject().getProjectManager().getEmail());
    }
}
