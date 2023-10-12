package com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistoryField;

import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUserService;
import com.mcr.bugtracker.BugTrackerApplication.ticket.Ticket;
import com.mcr.bugtracker.BugTrackerApplication.ticket.TicketEditViewDto;
import com.mcr.bugtracker.BugTrackerApplication.ticket.TicketRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@AllArgsConstructor
public class TicketHistoryFieldService {
    private final TicketHistoryFieldRepository ticketHistoryFieldRepository;
    private final AppUserService appUserService;
    public void saveChangeOfTitle(Ticket ticket, Ticket ticketWithUpdatedData) {
        if(ticket.getTitle() != null && !ticket.getTitle().equals(ticketWithUpdatedData.getTitle())) {
            save(new TicketHistoryField(PropertyEnum.TITLE.getName(),
                    ticket.getTitle(),
                    ticketWithUpdatedData.getTitle(),
                    ticket,
                    LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)));
            ticket.setTitle(ticketWithUpdatedData.getTitle());
        }
        else if(ticket.getTitle() == null) {
            ticket.setTitle(ticketWithUpdatedData.getTitle());
        }
    }

    public void saveChangeOfDescription(Ticket ticket, Ticket ticketWithUpdatedData) {
        if(ticket.getDescription() != null && !ticket.getDescription().equals(ticketWithUpdatedData.getDescription())) {
            save(new TicketHistoryField(PropertyEnum.DESCRITPION.getName(),
                    ticket.getDescription(),
                    ticketWithUpdatedData.getDescription(),
                    ticket,
                    LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)));
            ticket.setDescription(ticketWithUpdatedData.getDescription());
        }
        else if(ticket.getDescription() == null) {
            ticket.setDescription(ticketWithUpdatedData.getDescription());
        }
    }
    public void saveChangeOfDeveloper(Ticket ticket, TicketEditViewDto ticketDto) {
        if(ticket.getAssignedDeveloper() != null &&
                !ticket.getAssignedDeveloper().getId().equals(ticketDto.getDeveloper().getId())) {
            save(new TicketHistoryField(PropertyEnum.DEVELOPER.getName(),
                    ticket.getAssignedDeveloper().getWholeName(),
                    ticketDto.getDeveloper().getWholeName(),
                    ticket,
                    LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)));
            if(ticketDto.getDeveloper() == null) {
                ticket.setAssignedDeveloper(null);
            }
            else {
                ticket.setAssignedDeveloper(appUserService.findById(ticketDto.getDeveloper().getId()));
            }
        }
        else if(ticket.getAssignedDeveloper() == null && ticketDto.getDeveloper() != null) {
            ticket.setAssignedDeveloper(appUserService.findById(ticketDto.getDeveloper().getId()));
        }
    }

    public void saveChangeOfType(Ticket ticket, Ticket ticketWithUpdatedData) {
        if(ticket.getType() != null && !ticket.getType().equals(ticketWithUpdatedData.getType())) {
            save(new TicketHistoryField(PropertyEnum.TYPE.getName(),
                    ticket.getType(),
                    ticketWithUpdatedData.getType(),
                    ticket,
                    LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)));
            ticket.setType(ticketWithUpdatedData.getType());
        }
        else if(ticket.getType() == null) {
            ticket.setType(ticketWithUpdatedData.getType());
        }
    }

    public void saveChangeOfPriority(Ticket ticket, Ticket ticketWithUpdatedData) {
        if(ticket.getPriority() != null && !ticket.getPriority().equals(ticketWithUpdatedData.getPriority())) {
            save(new TicketHistoryField(PropertyEnum.PRIORITY.getName(),
                    ticket.getPriority(),
                    ticketWithUpdatedData.getPriority(),
                    ticket,
                    LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)));
            ticket.setPriority(ticketWithUpdatedData.getPriority());
        }
        else if(ticket.getPriority() == null) {
            ticket.setPriority(ticketWithUpdatedData.getPriority());
        }
    }

    public void saveChangeOfStatus(Ticket ticket, Ticket ticketWithUpdatedData) {
        if(ticket.getStatus() != null && !ticket.getStatus().equals(ticketWithUpdatedData.getStatus())) {
            save(new TicketHistoryField(PropertyEnum.STATUS.getName(),
                    ticket.getStatus(),
                    ticketWithUpdatedData.getStatus(),
                    ticket,
                    LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)));
            ticket.setStatus(ticketWithUpdatedData.getStatus());
        }
        else if(ticket.getStatus() == null) {
            ticket.setStatus(ticketWithUpdatedData.getStatus());
        }
    }

    public void save(TicketHistoryField field) {
        ticketHistoryFieldRepository.save(field);
    }
}
