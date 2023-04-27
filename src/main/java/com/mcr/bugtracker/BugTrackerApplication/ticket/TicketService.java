package com.mcr.bugtracker.BugTrackerApplication.ticket;

import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketFieldsEnums.Type;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistory.TicketHistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketHistoryRepository ticketHistoryRepository;

    public void saveTicket(TicketRequest request) {
        ticketHistoryRepository.save(request.getTicketHistory());
        ticketRepository.save(new Ticket(request.getTitle(),
                                        request.getDescription(),
                                        request.getPriority(),
                                        request.getStatus(),
                                        request.getType(),
                                        request.getTicketHistory()));
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Optional<Ticket> findById(Long ticketId) {
        return ticketRepository.findById(ticketId);
    }

    public Ticket saveTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public List<Type> getTicketTypes() {
        return List.of(Type.BUGS__ERRORS,
                Type.OTHER_COMMENTS,
                Type.FEATURE_REQUESTS,
                Type.TRAINING__DOCUMENT_REQUESTS);
    }
}
