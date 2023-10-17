package com.mcr.bugtracker.BugTrackerApplication.ticket;

import com.mcr.bugtracker.BugTrackerApplication.Exceptions.ApiForbiddenException;
import com.mcr.bugtracker.BugTrackerApplication.Exceptions.ApiNotFoundException;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUserDtoMapper;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUserRepository;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUserService;
import com.mcr.bugtracker.BugTrackerApplication.project.Project;
import com.mcr.bugtracker.BugTrackerApplication.ticket.commentary.CommentaryService;
import com.mcr.bugtracker.BugTrackerApplication.ticket.commentary.CommentsForTicketDetailsViewDto;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistoryField.TicketForTicketEditViewMapper;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistoryField.TicketHistoryField;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistoryField.TicketHistoryFieldService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final AppUserRepository appUserRepository;
    private final CommentaryService commentaryService;
    private final AppUserService appUserService;
    private final TicketHistoryFieldService ticketHistoryFieldService;
    private final AllTicketsViewMapper allTicketsViewMapper;
    private final TicketForTicketDetailsViewDtoMapper ticketForTicketDetailsViewDtoMapper;
    private final TicketForTicketEditViewMapper ticketForTicketEditViewMapper;
    private final AppUserDtoMapper appUserDtoMapper;
    public void deleteTicket(Long ticketId) {
        validateTicketExistence(ticketId);
        validateUserPermissionForTicketDelete(ticketRepository.findById(ticketId).orElseThrow());
        ticketRepository.deleteById(ticketId);
    }
    public List<AllTicketsViewDto> getAllTicketsConnectedToUser() {
        AppUser user = appUserService.getUserFromContext().orElseThrow();
        if(user.getSRole().equals("Admin") && user.isDemo()) {
            return ticketRepository.findAll()
                    .stream()
                    .filter(ticket -> ticket.getOptionalSubmitter().map(AppUser::isDemo).orElse(false))
                    .map(allTicketsViewMapper)
                    .collect(Collectors.toList());
        }
        else if(user.getSRole().equals("Admin")) {
            return ticketRepository.findAll().stream()
                    .map(allTicketsViewMapper)
                    .collect(Collectors.toList());
        }
        else {
            return Stream.concat(user.getAssignedTickets().stream(), user.getSubmittedTickets().stream())
                    .map(allTicketsViewMapper)
                    .collect(Collectors.toList());
        }
    }
    public TicketDetailsViewDto getDemandedDataForTicketDetailsView(Long ticketId) {
        validateTicketExistence(ticketId);
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow();
        validateUserPermissionForTicketDetails(ticket);
        return new TicketDetailsViewDto(ticketForTicketDetailsViewDtoMapper.apply(ticket),
                commentaryService.getCommentsWithDemandedData(ticket.getComments())
                        .stream()
                        .sorted(Comparator.comparing(CommentsForTicketDetailsViewDto::getCreated).reversed())
                        .collect(Collectors.toList()),
                ticket.getTicketHistoryFields()
                        .stream()
                        .sorted(Comparator.comparing(TicketHistoryField::getDateChanged).reversed())
                        .collect(Collectors.toList()));
    }
    private void validateUserPermissionForTicketDetails(Ticket ticket) {
        AppUser currentUser = appUserService.getUserFromContext().orElseThrow();
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
        if(ticketRepository.findById(ticketId).isEmpty()) {
            throw new ApiNotFoundException("There is no such resource");
        }
    }
    public TicketEditViewDto getDataForTicketEditView(Long ticketId) {
        validateTicketExistence(ticketId);
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow();
        validateUserPermissionForTicketEdit(ticket);
        return new TicketEditViewDto(ticketForTicketEditViewMapper.apply(ticket),
                appUserDtoMapper.apply(ticket.getAssignedDeveloper()),
                ticket.getProject().getProjectPersonnel().stream()
                        .filter(user -> user.getSRole().equals("Developer"))
                        .map(appUserDtoMapper)
                        .collect(Collectors.toList()));
    }
    private void validateUserPermissionForTicketEdit(Ticket ticket) {
        AppUser currentUser = appUserService.getUserFromContext().orElseThrow();
        if(!currentUser.equals(ticket.getProject().getProjectManager()) && !currentUser.equals(ticket.getSubmitter()) &&
            !currentUser.getSRole().equals("Admin")) {
            throw new ApiForbiddenException("You do not have permission for this resource");
        }
    }
    private void validateUserPermissionForTicketDelete(Ticket ticket) {
        AppUser currentUser = appUserService.getUserFromContext().orElseThrow();
        if(!currentUser.equals(ticket.getProject().getProjectManager()) && !currentUser.getSRole().equals("Admin") &&
            !currentUser.equals(ticket.getSubmitter())) {
            throw new ApiForbiddenException("You do not have permission for this resource");
        }
    }
    public void updateTicketData(TicketEditViewDto ticketEditViewDto) {
        TicketForTicketEditViewDto ticketWithUpdatedData = ticketEditViewDto.getTicket();
        validateTicketExistence(ticketWithUpdatedData.getId());
        Ticket ticket = ticketRepository.findById(ticketWithUpdatedData.getId()).orElseThrow();
        validateUserPermissionForTicketEdit(ticket);
        ticketHistoryFieldService.saveChangeOfTitle(ticket, ticketWithUpdatedData);
        ticketHistoryFieldService.saveChangeOfDescription(ticket, ticketWithUpdatedData);
        ticketHistoryFieldService.saveChangeOfDeveloper(ticket, ticketEditViewDto);
        ticketHistoryFieldService.saveChangeOfPriority(ticket, ticketWithUpdatedData);
        ticketHistoryFieldService.saveChangeOfType(ticket, ticketWithUpdatedData);
        ticketHistoryFieldService.saveChangeOfStatus(ticket, ticketWithUpdatedData);
        ticketRepository.save(ticket);
    }
    public Ticket createNewTicket(Project project) {
        Ticket ticket = new Ticket();
        ticket.setProject(project);
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setSubmitter(appUserRepository.findById(appUserService.getUserFromContext().orElseThrow().getId()).orElseThrow());
        return ticketRepository.save(ticket);
    }
}
