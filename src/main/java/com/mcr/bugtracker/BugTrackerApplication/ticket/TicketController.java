package com.mcr.bugtracker.BugTrackerApplication.ticket;

import com.mcr.bugtracker.BugTrackerApplication.project.Project;
import com.mcr.bugtracker.BugTrackerApplication.ticket.DTO.AllTicketsViewDto;
import com.mcr.bugtracker.BugTrackerApplication.ticket.DTO.TicketDetailsViewDto;
import com.mcr.bugtracker.BugTrackerApplication.ticket.DTO.TicketEditViewDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/ticket")
@AllArgsConstructor
@Slf4j
public class TicketController {
    private final TicketService ticketService;
    @PostMapping
    public Ticket createNewTicket(@RequestBody Project project) {
        return ticketService.createNewTicket(project);
    }
    @GetMapping
    public List<AllTicketsViewDto> getAllTicketsConnectedToUser() {
        return ticketService.getAllTicketsConnectedToUser();
    }
    @GetMapping("/details/{ticketId}")
    public TicketDetailsViewDto getTicketDataById(@PathVariable Long ticketId) {
        return ticketService.getDemandedDataForTicketDetailsView(ticketId);
    }
    @GetMapping("/edit/{ticketId}")
    public TicketEditViewDto getDataForTicketEditView(@PathVariable Long ticketId) {
        return ticketService.getDataForTicketEditView(ticketId);
    }
    @PutMapping("/edit/{ticketId}")
    public void updateTicketData(@RequestBody TicketEditViewDto ticket) {
        ticketService.updateTicketData(ticket);
    }
    @DeleteMapping
    public void deleteTicket(@RequestBody Long ticketId) {
        ticketService.deleteTicket(ticketId);
    }

}
