package com.mcr.bugtracker.BugTrackerApplication.ticket;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    public void saveTicket(TicketRequest request) {
        ticketRepository.save(new Ticket(request.getTitle(),
                                        request.getDescription(),
                                        request.getPriority(),
                                        request.getStatus(),
                                        request.getType()));
    }
}
