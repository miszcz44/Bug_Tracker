package com.mcr.bugtracker.BugTrackerApplication.ticket.Mapper;

import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import com.mcr.bugtracker.BugTrackerApplication.ticket.DTO.AllTicketsViewDto;
import com.mcr.bugtracker.BugTrackerApplication.ticket.Ticket;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class AllTicketsViewMapper implements Function<Ticket, AllTicketsViewDto> {

    @Override
    public AllTicketsViewDto apply(Ticket ticket) {
        return new AllTicketsViewDto(ticket.getId(),
                ticket.getTitle(),
                ticket.getProject().getName(),
                ticket.getProject().getProjectManager().getEmail(),
                ticket.getOptionalSubmitter().map(AppUser::getEmail).orElse("Not specified"),
                ticket.getOptionalDeveloper().map(AppUser::getEmail).orElse("Not specified"),
                ticket.getPriority(),
                ticket.getStatus(),
                ticket.getType(),
                ticket.getCreatedAt());
    }
}
