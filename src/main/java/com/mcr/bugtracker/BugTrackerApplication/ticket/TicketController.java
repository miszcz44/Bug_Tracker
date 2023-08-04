package com.mcr.bugtracker.BugTrackerApplication.ticket;

import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUserService;
import com.mcr.bugtracker.BugTrackerApplication.project.Project;
import com.mcr.bugtracker.BugTrackerApplication.project.ProjectService;
import com.mcr.bugtracker.BugTrackerApplication.registration.RegistrationRequest;
import com.mcr.bugtracker.BugTrackerApplication.ticket.attachment.Attachment;
import com.mcr.bugtracker.BugTrackerApplication.ticket.attachment.AttachmentService;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistoryField.TicketHistoryField;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistoryField.TicketHistoryFieldService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/ticket")
@AllArgsConstructor
@Slf4j
public class TicketController {

    private final TicketService ticketService;
    private final ProjectService projectService;
    private final AppUserService appUserService;
    private final TicketHistoryFieldService ticketHistoryFieldService;
    private final AttachmentService attachmentService;


    @PostMapping
    public ResponseEntity<?> createEmptyTicket() {
        Ticket ticket = new Ticket();
        ticketService.saveTicket(ticket);
        return ResponseEntity.ok(ticket);
    }

    @PostMapping("/to-project")
    public ResponseEntity<?> createEmptyTicketInProject(@RequestBody Long projectId) {
        Project project = projectService.findById(projectId).orElseThrow();
        Ticket ticket = new Ticket(project);
        ticket.setCreatedAt(LocalDateTime.now());
        ticketService.saveTicket(ticket);
        return ResponseEntity.ok(ticket);

    }

    @GetMapping
    public List<AllTicketsViewDto> getAllTicketsConnectedToUser() {
        return ticketService.getAllTicketsConnectedToUser();
    }



    @GetMapping("{ticketId}")
    public TicketDetailsViewDto getTicketDataById(@PathVariable Long ticketId) {
        return ticketService.getDemandedDataForProjectDetailsView(ticketId);
    }

    @GetMapping("/edit/{ticketId}")
    public TicketEditViewDto getDataForTicketEditView(@PathVariable Long ticketId) {
        return ticketService.getDataForTicketEditView(ticketId);
    }

    @PutMapping("{ticketId}")
    public void updateTicketData(@RequestBody TicketEditViewDto ticket) {
        ticketService.updateTicketData(ticket);
    }

    @PutMapping("{ticketId}/add-developer-to-ticket")
    public ResponseEntity<?> addDeveloperToTicket(@RequestBody String developerEmail, @PathVariable Long ticketId) {
        Ticket ticket = ticketService.findById(ticketId).orElseThrow();
        if(ticket.getAssignedDeveloper() != null) {
            AppUser currentDeveloper = ticket.getAssignedDeveloper();
            ticketService.assignDeveloperToTicketByEmail(ticket, developerEmail);
            ticketHistoryFieldService.saveChangeOfDeveloper(ticket, currentDeveloper);
        }
        ticketService.assignDeveloperToTicketByEmail(ticket, developerEmail);
        return ResponseEntity.ok(ticketService.saveTicket(ticket));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTicket(@RequestBody Long ticketId) {
        ticketService.deleteTicket(ticketId);
        return ResponseEntity.ok("deleted");
    }

}
