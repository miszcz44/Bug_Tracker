package com.mcr.bugtracker.BugTrackerApplication.ticket.DTO;

import com.mcr.bugtracker.BugTrackerApplication.ticket.commentary.CommentsForTicketDetailsViewDto;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistoryField.TicketHistoryField;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistoryField.TicketHistoryFieldForTicketDetailsDto;

import java.util.List;

public class TicketDetailsViewDto {
    private TicketForTicketDetailsViewDto ticket;
    private List<CommentsForTicketDetailsViewDto> comments;
    private List<TicketHistoryFieldForTicketDetailsDto> ticketHistoryField;
    public TicketDetailsViewDto(TicketForTicketDetailsViewDto ticket, List<CommentsForTicketDetailsViewDto> comments, List<TicketHistoryFieldForTicketDetailsDto> ticketHistoryField) {
        this.ticket = ticket;
        this.comments = comments;
        this.ticketHistoryField = ticketHistoryField;
    }
    public TicketForTicketDetailsViewDto getTicket() {
        return ticket;
    }
    public List<CommentsForTicketDetailsViewDto> getComments() {
        return comments;
    }
    public List<TicketHistoryFieldForTicketDetailsDto> getTicketHistoryField() {
        return ticketHistoryField;
    }
}
