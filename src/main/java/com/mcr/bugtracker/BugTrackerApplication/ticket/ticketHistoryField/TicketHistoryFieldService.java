package com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistoryField;

import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import com.mcr.bugtracker.BugTrackerApplication.ticket.Ticket;
import com.mcr.bugtracker.BugTrackerApplication.ticket.TicketRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class TicketHistoryFieldService {
    private final TicketHistoryFieldRepository ticketHistoryFieldRepository;
    private final TicketRepository ticketRepository;

    public List<TicketHistoryField> getAllFieldsByTicketId(Long id) {
        return ticketHistoryFieldRepository.findAllByTicketId(id);
    }

    public void retrieveDataForHistoryFields(Ticket ticket) {
        Ticket oldTicket = ticketRepository.findById(ticket.getId()).orElseThrow();
        saveChangeOfType(ticket, oldTicket);
        saveChangeOfPriority(ticket, oldTicket);
        saveChangeOfStatus(ticket, oldTicket);
    }

    public void saveChangeOfDeveloper(Ticket ticket, AppUser oldDeveloper) {
        ticketHistoryFieldRepository.save(new TicketHistoryField("Developer",
                    oldDeveloper.getEmail(),
                    ticket.getAssignedDeveloper().getEmail(),
                    ticket,
                    LocalDateTime.now()));
    }

    public void saveChangeOfType(Ticket ticket, Ticket oldTicket) {
        if(oldTicket.getType() != null && !oldTicket.getType().equals(ticket.getType())) {
            ticketHistoryFieldRepository.save(new TicketHistoryField("Type",
                    oldTicket.getType(),
                    ticket.getType(),
                    ticket,
                    LocalDateTime.now()));
        }
    }

    public void saveChangeOfPriority(Ticket ticket, Ticket oldTicket) {
        if(oldTicket.getPriority() != null && !oldTicket.getPriority().equals(ticket.getPriority())) {
            ticketHistoryFieldRepository.save(new TicketHistoryField("Priority",
                    oldTicket.getPriority(),
                    ticket.getPriority(),
                    ticket,
                    LocalDateTime.now()));
        }
    }

    public void saveChangeOfStatus(Ticket ticket, Ticket oldTicket) {
        if(oldTicket.getStatus() != null && !oldTicket.getStatus().equals(ticket.getStatus())) {
            ticketHistoryFieldRepository.save(new TicketHistoryField("Status",
                    oldTicket.getStatus(),
                    ticket.getStatus(),
                    ticket,
                    LocalDateTime.now()));
        }
    }
}
