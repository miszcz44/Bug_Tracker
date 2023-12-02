package com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistoryField;

import com.mcr.bugtracker.BugTrackerApplication.util.DateAndTimeFormatter;
import org.springframework.stereotype.Service;

import java.util.function.Function;
@Service
public class TicketHistoryFieldForTicketDetailsMapper implements Function<TicketHistoryField, TicketHistoryFieldForTicketDetailsDto> {
    @Override
    public TicketHistoryFieldForTicketDetailsDto apply(TicketHistoryField ticketHistoryField) {
        return new TicketHistoryFieldForTicketDetailsDto(ticketHistoryField.getProperty(),
                ticketHistoryField.getOldValue(),
                ticketHistoryField.getNewValue(),
                DateAndTimeFormatter.convertDateAndTimeToCurrentUserZone(ticketHistoryField.getDateChanged()));
    }
}
