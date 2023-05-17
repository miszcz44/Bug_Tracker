package com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistory;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TicketHistoryService {

    private final TicketHistoryRepository ticketHistoryRepository;
    public void saveTicketHistory(TicketHistoryRequest request) {
        ticketHistoryRepository.save(new TicketHistory());
    }
}