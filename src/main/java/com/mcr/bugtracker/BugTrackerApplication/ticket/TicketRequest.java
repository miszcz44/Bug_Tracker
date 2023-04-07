package com.mcr.bugtracker.BugTrackerApplication.ticket;

import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketFieldsEnums.Priority;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketFieldsEnums.ProgressStatus;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketFieldsEnums.Type;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistory.TicketHistory;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class TicketRequest {

    private final String title;
    private final String description;
    private final Priority priority;
    private final ProgressStatus status;
    private final Type type;
    private final TicketHistory ticketHistory;

}
