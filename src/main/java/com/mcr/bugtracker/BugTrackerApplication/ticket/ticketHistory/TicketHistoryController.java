package com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistory;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/history")
@AllArgsConstructor
public class TicketHistoryController {

    private final TicketHistoryService ticketHistoryService;
    @PostMapping
    public void saveTicketHistory(@RequestBody TicketHistoryRequest request) {
        ticketHistoryService.saveTicketHistory(request);
    }
}
