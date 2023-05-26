package com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistoryField;

import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import com.mcr.bugtracker.BugTrackerApplication.ticket.Ticket;
import com.mcr.bugtracker.BugTrackerApplication.ticket.TicketRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TicketHistoryFieldService {
    private final TicketHistoryFieldRepository ticketHistoryFieldRepository;
    private final TicketRepository ticketRepository;

    public void retrieveDataForHistoryFields(Ticket ticket) {
        Ticket oldTicket = ticketRepository.findById(ticket.getId()).orElseThrow();

    }

    public void saveChangeOfDeveloper(Ticket ticket, AppUser oldDeveloper) {
        TicketHistoryField ticketHistoryField =
                new TicketHistoryField("Developer",
                        oldDeveloper.getEmail(),
                        ticket.getAssignedDeveloper().getEmail(),
                        ticket);
            ticketHistoryFieldRepository.save(ticketHistoryField);
    }
}
