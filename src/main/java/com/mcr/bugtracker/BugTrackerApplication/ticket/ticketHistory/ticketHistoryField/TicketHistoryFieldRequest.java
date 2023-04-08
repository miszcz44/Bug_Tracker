package com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistory.ticketHistoryField;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class TicketHistoryFieldRequest {
    private String property;
    private String oldValue;
    private String newValue;
}
