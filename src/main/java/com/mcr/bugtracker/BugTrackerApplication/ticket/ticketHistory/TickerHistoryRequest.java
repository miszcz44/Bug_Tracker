package com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistory;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class TickerHistoryRequest {
    private final String property;
    private final String oldValue;
    private final String newValue;
}
