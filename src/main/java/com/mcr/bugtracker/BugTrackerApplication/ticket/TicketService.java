package com.mcr.bugtracker.BugTrackerApplication.ticket;

import com.mcr.bugtracker.BugTrackerApplication.Exceptions.ApiForbiddenException;
import com.mcr.bugtracker.BugTrackerApplication.Exceptions.ApiNotFoundException;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUserRepository;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUserService;
import com.mcr.bugtracker.BugTrackerApplication.project.Project;
import com.mcr.bugtracker.BugTrackerApplication.ticket.commentary.CommentaryService;
import com.mcr.bugtracker.BugTrackerApplication.ticket.commentary.CommentsForTicketDetailsViewDto;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistoryField.PropertyEnum;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistoryField.TicketHistoryField;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistoryField.TicketHistoryFieldService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
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
        if(user.getSRole().equals("Admin") && user.isDemo()) {
            tickets = ticketRepository.findAll()
                    .stream()
                    .filter(ticket -> ticket.getSubmitter().isDemo())
                    .collect(Collectors.toList());
        }
        else if(user.getSRole().equals("Admin")) {
            tickets = ticketRepository.findAll();
        }
        else {
            tickets = Stream.concat(user.getAssignedTickets().stream(), user.getSubmittedTickets().stream()).collect(Collectors.toList());
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
        List<CommentsForTicketDetailsViewDto> commentsSortedByDate = commentaryService.getCommentsWithDemandedData(ticket.getComments())
                .stream()
                .sorted(Comparator.comparing(CommentsForTicketDetailsViewDto::getCreated).reversed())
                .collect(Collectors.toList());
        return new TicketDetailsViewDto(ticketWithDemandedData,
                developerName,
                ticket.getSubmitter().getWholeName(),
                ticket.getSubmitter().getEmail(),
                ticket.getProject().getId(),
                ticket.getProject().getName(),
                ticket.getProject().getProjectManager().getEmail(),
                commentsSortedByDate,
                ticket.getTicketHistoryFields()
                        .stream()
                        .sorted(Comparator.comparing(TicketHistoryField::getDateChanged).reversed())
                        .collect(Collectors.toList()));

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
        Ticket ticket = ticketRepository.findById(ticketWithUpdatedData.getId()).get();
        validateUserPermissionForTicketEdit(ticket);
        ticketHistoryFieldService.saveChangeOfTitle(ticket, ticketWithUpdatedData);
        ticketHistoryFieldService.saveChangeOfDescription(ticket, ticketWithUpdatedData);
        ticketHistoryFieldService.saveChangeOfDeveloper(ticket, ticketDto);
        ticketHistoryFieldService.saveChangeOfPriority(ticket, ticketWithUpdatedData);
        ticketHistoryFieldService.saveChangeOfType(ticket, ticketWithUpdatedData);
        ticketHistoryFieldService.saveChangeOfStatus(ticket, ticketWithUpdatedData);
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
