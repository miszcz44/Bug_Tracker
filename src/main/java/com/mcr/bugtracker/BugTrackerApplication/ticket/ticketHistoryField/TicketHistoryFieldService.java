package com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistoryField;

import com.mcr.bugtracker.BugTrackerApplication.appuser.DTO.AppUserDto;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUserService;
import com.mcr.bugtracker.BugTrackerApplication.ticket.Ticket;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@AllArgsConstructor
public class TicketHistoryFieldService {
    private final TicketHistoryFieldRepository ticketHistoryFieldRepository;
    private final AppUserService appUserService;
    public void saveChangeOfTitle(Ticket ticket, String title) {
        if(ticket.getTitle() != null && !ticket.getTitle().equals(title)) {
            save(new TicketHistoryField(PropertyEnum.TITLE.getName(),
                    ticket.getTitle(),
                    title,
                    ticket,
                    LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)));
            ticket.setTitle(title);
        }
        else if(ticket.getTitle() == null) {
            ticket.setTitle(title);
        }
    }
    public void saveChangeOfDescription(Ticket ticket, String description) {
        if(ticket.getDescription() != null && !ticket.getDescription().equals(description)) {
            save(new TicketHistoryField(PropertyEnum.DESCRIPTION.getName(),
                    ticket.getDescription(),
                    description,
                    ticket,
                    LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)));
            ticket.setDescription(description);
        }
        else if(ticket.getDescription() == null) {
            ticket.setDescription(description);
        }
    }
    public void saveChangeOfDeveloper(Ticket ticket, AppUserDto developer) {
        if(ticket.getAssignedDeveloper() != null &&
                !ticket.getAssignedDeveloper().getId().equals(developer.getId())) {
            save(new TicketHistoryField(PropertyEnum.DEVELOPER.getName(),
                    ticket.getAssignedDeveloper().getWholeName(),
                    developer.getWholeName(),
                    ticket,
                    LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)));
            ticket.setAssignedDeveloper(appUserService.findById(developer.getId()));
        }
        else if(ticket.getAssignedDeveloper() == null && developer != null) {
            ticket.setAssignedDeveloper(appUserService.findById(developer.getId()));
        }
    }
    public void saveChangeOfType(Ticket ticket, String type) {
        if(ticket.getType() != null && !ticket.getType().equals(type)) {
            save(new TicketHistoryField(PropertyEnum.TYPE.getName(),
                    ticket.getType(),
                    type,
                    ticket,
                    LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)));
            ticket.setType(type);
        }
        else if(ticket.getType() == null) {
            ticket.setType(type);
        }
    }
    public void saveChangeOfPriority(Ticket ticket, String priority) {
        if(ticket.getPriority() != null && !ticket.getPriority().equals(priority)) {
            save(new TicketHistoryField(PropertyEnum.PRIORITY.getName(),
                    ticket.getPriority(),
                    priority,
                    ticket,
                    LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)));
            ticket.setPriority(priority);
        }
        else if(ticket.getPriority() == null) {
            ticket.setPriority(priority);
        }
    }
    public void saveChangeOfStatus(Ticket ticket, String status) {
        if(ticket.getStatus() != null && !ticket.getStatus().equals(status)) {
            save(new TicketHistoryField(PropertyEnum.STATUS.getName(),
                    ticket.getStatus(),
                    status,
                    ticket,
                    LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)));
            ticket.setStatus(status);
        }
        else if(ticket.getStatus() == null) {
            ticket.setStatus(status);
        }
    }
    public void save(TicketHistoryField field) {
        ticketHistoryFieldRepository.save(field);
    }
}
