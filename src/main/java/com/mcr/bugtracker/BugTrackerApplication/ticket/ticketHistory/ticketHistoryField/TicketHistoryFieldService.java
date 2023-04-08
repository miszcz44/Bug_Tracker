package com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistory.ticketHistoryField;

import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistory.TicketHistory;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistory.TicketHistoryRepository;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistory.TicketHistoryRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TicketHistoryFieldService {
    private final TicketHistoryFieldRepository ticketHistoryFieldRepository;
    public void saveTicketHistoryField(TicketHistoryFieldRequest request) {
        ticketHistoryFieldRepository.save(new TicketHistoryField(request.getProperty(),
                                                                request.getOldValue(),
                                                                request.getNewValue()));
    }
}
