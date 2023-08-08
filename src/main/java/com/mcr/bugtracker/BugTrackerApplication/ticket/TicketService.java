package com.mcr.bugtracker.BugTrackerApplication.ticket;

import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUserRepository;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUserService;
import com.mcr.bugtracker.BugTrackerApplication.project.Project;
import com.mcr.bugtracker.BugTrackerApplication.project.ProjectService;
import com.mcr.bugtracker.BugTrackerApplication.ticket.commentary.CommentaryService;
import com.mcr.bugtracker.BugTrackerApplication.ticket.commentary.CommentsForTicketDetailsViewDto;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketFieldsEnums.Type;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistoryField.TicketHistoryField;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistoryField.TicketHistoryFieldService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    private final TicketHistoryFieldService ticketHistoryFieldService;


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
        AppUser developerWithDemandedData = null;
        if(ticket.getAssignedDeveloper() != null) {
            developerWithDemandedData = appUserService.getDeveloperForTicketEditView(ticket.getAssignedDeveloper());
        }
        List<AppUser> possibleDevelopersWithDemandedData = appUserService.getProjectDevelopers(ticket.getProject());
        return new TicketEditViewDto(ticketWithDemandedData, ticket.getProject().getName(),
                developerWithDemandedData, possibleDevelopersWithDemandedData);
    }

    // this needs refactoring XD TODO
    public void updateTicketData(TicketEditViewDto ticketDto) {
        Ticket ticketWithUpdataData = ticketDto.getTicket();
        Ticket ticket = ticketRepository.findById(ticketWithUpdataData.getId()).orElseThrow();
        if(!ticket.getTitle().equals(ticketWithUpdataData.getTitle())) {
            ticketHistoryFieldService.save(new TicketHistoryField("Title",
                    ticket.getTitle(),
                    ticketWithUpdataData.getTitle(),
                    ticket,
                    LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)));
            ticket.setTitle(ticketWithUpdataData.getTitle());
        }
        if(!ticket.getDescription().equals(ticketWithUpdataData.getDescription())) {
            ticketHistoryFieldService.save(new TicketHistoryField("Description",
                    ticket.getDescription(),
                    ticketWithUpdataData.getDescription(),
                    ticket,
                    LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)));
            ticket.setDescription(ticketWithUpdataData.getDescription());
        }
        if(!ticket.getAssignedDeveloper().getId().equals(ticketDto.getDeveloper().getId())) {
            ticketHistoryFieldService.save(new TicketHistoryField("Developer",
                    ticket.getAssignedDeveloper().getWholeName(),
                    ticketDto.getDeveloper().getWholeName(),
                    ticket,
                    LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)));
            ticket.setAssignedDeveloper(appUserService.findById(ticketDto.getDeveloper().getId()));
        }
        if(!ticket.getPriority().equals(ticketWithUpdataData.getPriority())) {
            ticketHistoryFieldService.save(new TicketHistoryField("Priority", // property should be enum !!! TODO
                    ticket.getPriority(),
                    ticketWithUpdataData.getPriority(),
                    ticket,
                    LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)));
            ticket.setPriority(ticketWithUpdataData.getPriority());
        }
        if(!ticket.getType().equals(ticketWithUpdataData.getType())) {
            ticketHistoryFieldService.save(new TicketHistoryField("Type", // property should be enum !!! TODO
                    ticket.getType(),
                    ticketWithUpdataData.getType(),
                    ticket,
                    LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)));
            ticket.setType(ticketWithUpdataData.getType());
        }
        if(!ticket.getStatus().equals(ticketWithUpdataData.getStatus())) {
            ticketHistoryFieldService.save(new TicketHistoryField("Status", // property should be enum !!! TODO
                    ticket.getStatus(),
                    ticketWithUpdataData.getStatus(),
                    ticket,
                    LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)));
            ticket.setStatus(ticketWithUpdataData.getStatus());
        }
        ticketRepository.save(ticket);
    }

    public Ticket createNewTicket(Project project) {
        Ticket ticket = new Ticket();
        ticket.setProject(project);
        return ticketRepository.save(ticket);
    }
}
