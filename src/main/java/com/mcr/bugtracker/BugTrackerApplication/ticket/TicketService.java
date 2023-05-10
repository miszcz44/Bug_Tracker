package com.mcr.bugtracker.BugTrackerApplication.ticket;

import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUserRepository;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketFieldsEnums.Type;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistory.TicketHistoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketHistoryRepository ticketHistoryRepository;
    private final AppUserRepository appUserRepository;

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

    public void assignDeveloperToTicketByEmail(Ticket ticket, String developerEmail) {
        String emailWithoutQuotationMarks = developerEmail.substring(1, developerEmail.length() - 1);
        log.info(emailWithoutQuotationMarks);
        ticket.setAssignedDeveloper(appUserRepository.findByEmail(emailWithoutQuotationMarks).orElseThrow());
    }

    public Optional<AppUser> getUserFromContext() {
        AppUser user = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return appUserRepository.findById(user.getId());
    }

    public void setSubmitterToTicket(Ticket ticket) {
        ticket.setSubmitter(getUserFromContext().orElseThrow());
    }

    public void deleteTicket(Long ticketId) {
        ticketRepository.deleteById(ticketId);
    }
}
