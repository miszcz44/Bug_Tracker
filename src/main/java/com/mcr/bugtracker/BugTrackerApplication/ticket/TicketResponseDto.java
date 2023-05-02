package com.mcr.bugtracker.BugTrackerApplication.ticket;

import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketFieldsEnums.Priority;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketFieldsEnums.Type;

public class TicketResponseDto {

    private Ticket ticket;
    private Type[] types = Type.values();
    private Priority[] priorities = Priority.values();

    public TicketResponseDto(Ticket ticket) {
        super();
        this.ticket = ticket;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Type[] getTypes() {
        return types;
    }

    public void setTypes(Type[] types) {
        this.types = types;
    }

    public Priority[] getPriorities() {
        return priorities;
    }

    public void setPriorities(Priority[] priorities) {
        this.priorities = priorities;
    }
}
