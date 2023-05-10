package com.mcr.bugtracker.BugTrackerApplication.ticket;

import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUserService;
import com.mcr.bugtracker.BugTrackerApplication.project.Project;
import com.mcr.bugtracker.BugTrackerApplication.project.ProjectService;
import com.mcr.bugtracker.BugTrackerApplication.registration.RegistrationRequest;
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
    public ResponseEntity<?> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }



    @GetMapping("{ticketId}")
    public ResponseEntity<?> getTicketDataById(@PathVariable Long ticketId) {
        Ticket ticketOpt = ticketService.findById(ticketId).orElseThrow();
        List<AppUser> usersAssignedToProject = appUserService.findAllUsersAssignedToProject(ticketOpt.getProject().getId());
        return ResponseEntity.ok(new TicketResponseDto(ticketOpt, usersAssignedToProject));
    }

    @PutMapping("{ticketId}")
    public ResponseEntity<?> updateTicketData(@RequestBody Ticket ticket, @PathVariable Long ticketId) {
        ticketService.setSubmitterToTicket(ticket);
        Ticket updatedTicket = ticketService.saveTicket(ticket);
        return ResponseEntity.ok(updatedTicket);
    }

    @PutMapping("{ticketId}/add-developer-to-ticket")
    public ResponseEntity<?> addDeveloperToTicket(@RequestBody String developerEmail, @PathVariable Long ticketId) {
        Ticket ticket = ticketService.findById(ticketId).orElseThrow();
        ticketService.assignDeveloperToTicketByEmail(ticket, developerEmail);
        ticketService.saveTicket(ticket);
        return ResponseEntity.ok(ticket);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTicket(@RequestBody Long ticketId) {
        ticketService.deleteTicket(ticketId);
        return ResponseEntity.ok("deleted");
    }

}
