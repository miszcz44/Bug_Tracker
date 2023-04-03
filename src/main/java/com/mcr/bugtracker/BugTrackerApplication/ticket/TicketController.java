package com.mcr.bugtracker.BugTrackerApplication.ticket;

import com.mcr.bugtracker.BugTrackerApplication.registration.RegistrationRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/ticket")
@AllArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public void saveTicket(@RequestBody TicketRequest request) {
        ticketService.saveTicket(request);
    }

}
