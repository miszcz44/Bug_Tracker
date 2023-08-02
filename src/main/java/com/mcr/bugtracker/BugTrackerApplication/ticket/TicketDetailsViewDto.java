package com.mcr.bugtracker.BugTrackerApplication.ticket;

import com.mcr.bugtracker.BugTrackerApplication.ticket.commentary.CommentsForTicketDetailsViewDto;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistoryField.TicketHistoryField;
import java.util.List;

public class TicketDetailsViewDto {
    private Ticket ticket;
    private List<CommentsForTicketDetailsViewDto> comments;
    private TicketHistoryField ticketHistoryField;

    public TicketDetailsViewDto(Ticket ticket, List<CommentsForTicketDetailsViewDto> comments, TicketHistoryField ticketHistoryField) {
        this.ticket = ticket;
        this.comments = comments;
        this.ticketHistoryField = ticketHistoryField;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public List<CommentsForTicketDetailsViewDto> getComments() {
        return comments;
    }

    public void setComments(List<CommentsForTicketDetailsViewDto> comments) {
        this.comments = comments;
    }

    public TicketHistoryField getTicketHistoryField() {
        return ticketHistoryField;
    }

    public void setTicketHistoryField(TicketHistoryField ticketHistoryField) {
        this.ticketHistoryField = ticketHistoryField;
    }
}
