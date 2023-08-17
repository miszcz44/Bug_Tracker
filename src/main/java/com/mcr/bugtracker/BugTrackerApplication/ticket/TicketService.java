package com.mcr.bugtracker.BugTrackerApplication.ticket;

import com.mcr.bugtracker.BugTrackerApplication.Exceptions.ApiForbiddenException;
import com.mcr.bugtracker.BugTrackerApplication.Exceptions.ApiNotFoundException;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUserRepository;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUserService;
import com.mcr.bugtracker.BugTrackerApplication.project.Project;
import com.mcr.bugtracker.BugTrackerApplication.ticket.commentary.CommentaryService;
import com.mcr.bugtracker.BugTrackerApplication.ticket.commentary.CommentsForTicketDetailsViewDto;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistoryField.TicketHistoryField;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistoryField.TicketHistoryFieldService;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
@Slf4j
public class TicketService {

    private final TicketRepository ticketRepository;
    private final AppUserRepository appUserRepository;
    private final CommentaryService commentaryService;
    private final AppUserService appUserService;
    private final TicketHistoryFieldService ticketHistoryFieldService;
    private final AllTicketsViewMapper allTicketsViewMapper;


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
        validateTicketExistence(ticketId);
        validateUserPermissionForTicketDelete(ticketRepository.findById(ticketId).get());
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
        List<Ticket> tickets;
        if(user.getSRole().equals("Admin")) {
            tickets = ticketRepository.findAll();
        }
        else {
            tickets = Stream.concat(user.getAssignedTickets().stream(), user.getSubmittedTickets().stream()).toList();
        }
        List<AllTicketsViewDto> ticketsForAllTicketsView = new ArrayList<>();
        for(Ticket ticket : tickets) {
            ticketsForAllTicketsView.add(allTicketsViewMapper.apply(ticket));
        }
        return ticketsForAllTicketsView;
    }

    public TicketDetailsViewDto getDemandedDataForTicketDetailsView(Long ticketId) {
        validateTicketExistence(ticketId);
        Ticket ticket = ticketRepository.findById(ticketId).get();
        validateUserPermissionForTicketDetails(ticket);
        Ticket ticketWithDemandedData = new Ticket.Builder()
                .title(ticket.getTitle())
                .description(ticket.getDescription())
                .priority(ticket.getPriority())
                .status(ticket.getStatus())
                .type(ticket.getType())
                .createdAt(ticket.getCreatedAt().truncatedTo(ChronoUnit.SECONDS))
                .build();
        String developerName = "";
        if(ticket.getAssignedDeveloper() != null) {
            developerName = ticket.getAssignedDeveloper().getWholeName();
        }
        List<CommentsForTicketDetailsViewDto> comments = commentaryService.getCommentsWithDemandedData(ticket.getComments());
        return new TicketDetailsViewDto(ticketWithDemandedData,
                developerName,
                ticket.getSubmitter().getWholeName(),
                ticket.getSubmitter().getEmail(),
                ticket.getProject().getId(),
                ticket.getProject().getName(),
                ticket.getProject().getProjectManager().getEmail(),
                comments,
                ticket.getTicketHistoryFields());
    }

    private void validateUserPermissionForTicketDetails(Ticket ticket) {
        AppUser currentUser = getUserFromContext().orElseThrow();
        List<AppUser> otherProjectManagers = ticket.getProject().getProjectPersonnel().stream()
                .filter(obj -> obj.getSRole().equals("Project manager"))
                .collect(Collectors.toList());
        if(!currentUser.equals(ticket.getSubmitter()) && !currentUser.equals(ticket.getProject().getProjectManager()) &&
            !otherProjectManagers.contains(currentUser) && !currentUser.equals(ticket.getAssignedDeveloper()) &&
            !currentUser.getSRole().equals("Admin")) {
            throw new ApiForbiddenException("You do not have permission for this resource");
        }
    }

    private void validateTicketExistence(Long ticketId) {
        if(!ticketRepository.findById(ticketId).isPresent()) {
            throw new ApiNotFoundException("There is no such resource");
        }
    }

    public TicketEditViewDto getDataForTicketEditView(Long ticketId) {
        validateTicketExistence(ticketId);
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow();
        validateUserPermissionForTicketEdit(ticket);
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

    private void validateUserPermissionForTicketEdit(Ticket ticket) {
        AppUser currentUser = getUserFromContext().orElseThrow();
        if(!currentUser.equals(ticket.getProject().getProjectManager()) && !currentUser.equals(ticket.getSubmitter()) &&
            !currentUser.getSRole().equals("Admin")) {
            throw new ApiForbiddenException("You do not have permission for this resource");
        }
    }

    private void validateUserPermissionForTicketDelete(Ticket ticket) {
        AppUser currentUser = getUserFromContext().orElseThrow();
        if(!currentUser.equals(ticket.getProject().getProjectManager()) && !currentUser.getSRole().equals("Admin") &&
            !currentUser.equals(ticket.getSubmitter())) {
            throw new ApiForbiddenException("You do not have permission for this resource");
        }
    }

    // this needs refactoring XD TODO
    public void updateTicketData(TicketEditViewDto ticketDto) {
        Ticket ticketWithUpdatedData = ticketDto.getTicket();
        validateTicketExistence(ticketWithUpdatedData.getId());
        Ticket ticket = ticketRepository.findById(ticketWithUpdatedData.getId()).orElseThrow();
        validateUserPermissionForTicketEdit(ticket);
        if(ticket.getTitle() != null && !ticket.getTitle().equals(ticketWithUpdatedData.getTitle())) {
            ticketHistoryFieldService.save(new TicketHistoryField("Title",
                    ticket.getTitle(),
                    ticketWithUpdatedData.getTitle(),
                    ticket,
                    LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)));
            ticket.setTitle(ticketWithUpdatedData.getTitle());
        }
        else if(ticket.getTitle() == null) {
            ticket.setTitle(ticketWithUpdatedData.getTitle());
        }
        if(ticket.getDescription() != null && !ticket.getDescription().equals(ticketWithUpdatedData.getDescription())) {
            ticketHistoryFieldService.save(new TicketHistoryField("Description",
                    ticket.getDescription(),
                    ticketWithUpdatedData.getDescription(),
                    ticket,
                    LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)));
            ticket.setDescription(ticketWithUpdatedData.getDescription());
        }
        else if(ticket.getDescription() == null) {
            ticket.setDescription(ticketWithUpdatedData.getDescription());
        }
        if(ticket.getAssignedDeveloper() != null &&
                !ticket.getAssignedDeveloper().getId().equals(ticketDto.getDeveloper().getId())) {
            ticketHistoryFieldService.save(new TicketHistoryField("Developer",
                    ticket.getAssignedDeveloper().getWholeName(),
                    ticketDto.getDeveloper().getWholeName(),
                    ticket,
                    LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)));
            if(ticketDto.getDeveloper() == null) {
                ticket.setAssignedDeveloper(null);
            }
            else {
                ticket.setAssignedDeveloper(appUserService.findById(ticketDto.getDeveloper().getId()));
            }
        }
        else if(ticket.getAssignedDeveloper() == null && ticketDto.getDeveloper() != null) {
            ticket.setAssignedDeveloper(appUserService.findById(ticketDto.getDeveloper().getId()));
        }
        if(ticket.getPriority() != null && !ticket.getPriority().equals(ticketWithUpdatedData.getPriority())) {
            ticketHistoryFieldService.save(new TicketHistoryField("Priority", // property should be enum !!! TODO
                    ticket.getPriority(),
                    ticketWithUpdatedData.getPriority(),
                    ticket,
                    LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)));
            ticket.setPriority(ticketWithUpdatedData.getPriority());
        }
        else if(ticket.getPriority() == null) {
            ticket.setPriority(ticketWithUpdatedData.getPriority());
        }
        if(ticket.getType() != null && !ticket.getType().equals(ticketWithUpdatedData.getType())) {
            ticketHistoryFieldService.save(new TicketHistoryField("Type", // property should be enum !!! TODO
                    ticket.getType(),
                    ticketWithUpdatedData.getType(),
                    ticket,
                    LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)));
            ticket.setType(ticketWithUpdatedData.getType());
        }
        else if(ticket.getType() == null) {
            ticket.setType(ticketWithUpdatedData.getType());
        }
        if(ticket.getStatus() != null && !ticket.getStatus().equals(ticketWithUpdatedData.getStatus())) {
            ticketHistoryFieldService.save(new TicketHistoryField("Status", // property should be enum !!! TODO
                    ticket.getStatus(),
                    ticketWithUpdatedData.getStatus(),
                    ticket,
                    LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)));
            ticket.setStatus(ticketWithUpdatedData.getStatus());
        }
        else if(ticket.getStatus() == null) {
            ticket.setStatus(ticketWithUpdatedData.getStatus());
        }
        ticketRepository.save(ticket);
    }

    public Ticket createNewTicket(Project project) {
        Ticket ticket = new Ticket();
        ticket.setProject(project);
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setSubmitter(appUserRepository.findById(getUserFromContext().orElseThrow().getId()).orElseThrow());
        return ticketRepository.save(ticket);
    }
}
