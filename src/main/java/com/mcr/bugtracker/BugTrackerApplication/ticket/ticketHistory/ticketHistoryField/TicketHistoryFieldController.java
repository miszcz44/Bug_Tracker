package com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistory.ticketHistoryField;

import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistory.TicketHistoryRequest;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistory.TicketHistoryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/history-field")
@AllArgsConstructor
public class TicketHistoryFieldController {
    private final TicketHistoryFieldService ticketHistoryFieldService;
    @PostMapping
    public void saveTicketHistoryField(@RequestBody TicketHistoryFieldRequest request) {
        ticketHistoryFieldService.saveTicketHistoryField(request);
    }
}
