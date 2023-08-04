package com.mcr.bugtracker.BugTrackerApplication.ticket;

import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUserRepository;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUserService;
import com.mcr.bugtracker.BugTrackerApplication.project.Project;
import com.mcr.bugtracker.BugTrackerApplication.project.ProjectService;
import com.mcr.bugtracker.BugTrackerApplication.ticket.commentary.CommentaryService;
import com.mcr.bugtracker.BugTrackerApplication.ticket.commentary.CommentsForTicketDetailsViewDto;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketFieldsEnums.Type;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class TicketService {

    private final TicketRepository ticketRepository;
    private final AppUserRepository appUserRepository;
    private final CommentaryService commentaryService;
    private final AppUserService appUserService;


    public void saveTicket(TicketRequest request) {
        ticketRepository.save(new Ticket(request.getTitle(),
                                        request.getDescription(),
                                        request.getPriority(),
                                        request.getStatus(),
                                        request.getType()));
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

    public List<TicketForProjectViewDto> getDemandedTicketDataForProjectView(List<Ticket> tickets) {
        List<TicketForProjectViewDto> demandedTickets = new ArrayList<>();
        if(tickets != null) {
            for (Ticket ticket : tickets) {
                TicketForProjectViewDto demandedTicket = new TicketForProjectViewDto(ticket.getId(),
                        ticket.getTitle(),
                        ticket.getStatus(),
                        ticket.getCreatedAt().truncatedTo(ChronoUnit.SECONDS));
                if(ticket.getSubmitter() != null){
                    demandedTicket.setSubmitter(ticket.getSubmitter().getWholeName());
                }
                if(ticket.getAssignedDeveloper() != null){
                    demandedTicket.setDeveloper(ticket.getAssignedDeveloper().getWholeName());
                }
                demandedTickets.add(demandedTicket);
            }
        }
        return demandedTickets;
    }


    public List<AllTicketsViewDto> getAllTicketsConnectedToUser() {
        AppUser user = getUserFromContext().orElseThrow();
        List<AllTicketsViewDto> allTickets = new ArrayList<>();
        List<Ticket> tickets = user.getAssignedTicket();
        for(Ticket ticket : tickets) {
            allTickets.add(new AllTicketsViewDto(ticket.getId(),
                    ticket.getTitle(),
                    ticket.getProject().getName(),
                    ticket.getAssignedDeveloper().getWholeName(),
                    ticket.getPriority(),
                    ticket.getStatus(),
                    ticket.getType(),
                    ticket.getCreatedAt().truncatedTo(ChronoUnit.SECONDS)
                    ));
        }
        return allTickets;
    }

    public TicketDetailsViewDto getDemandedDataForProjectDetailsView(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow();
        Ticket ticketWithDemandedData = new Ticket.Builder()
                .title(ticket.getTitle())
                .description(ticket.getDescription())
                .priority(ticket.getPriority())
                .status(ticket.getStatus())
                .type(ticket.getType())
                .createdAt(ticket.getCreatedAt().truncatedTo(ChronoUnit.SECONDS))
                .build();
        List<CommentsForTicketDetailsViewDto> comments = commentaryService.getCommentsWithDemandedData(ticket.getComments());
        return new TicketDetailsViewDto(ticketWithDemandedData,
                ticket.getAssignedDeveloper().getWholeName(),
                ticket.getSubmitter().getWholeName(),
                ticket.getProject().getName(),
                comments,
                ticket.getTicketHistoryFields());
    }

    public TicketEditViewDto getDataForTicketEditView(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow();
        Ticket ticketWithDemandedData = new Ticket.Builder()
                .id(ticket.getId())
                .title(ticket.getTitle())
                .description(ticket.getDescription())
                .priority(ticket.getPriority())
                .status(ticket.getStatus())
                .type(ticket.getType())
                .build();
        AppUser developerWithDemandedData = appUserService.getDeveloperForTicketEditView(ticket.getAssignedDeveloper());
        List<AppUser> possibleDevelopersWithDemandedData = appUserService.getProjectDevelopers(ticket.getProject());
        return new TicketEditViewDto(ticketWithDemandedData, ticket.getProject().getName(),
                developerWithDemandedData, possibleDevelopersWithDemandedData);
    }
}
