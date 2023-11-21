package com.mcr.bugtracker.BugTrackerApplication.ticket;

import com.mcr.bugtracker.BugTrackerApplication.appuser.DTO.AppUserDto;
import com.mcr.bugtracker.BugTrackerApplication.ticket.DTO.TicketForTicketEditViewDto;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketFieldsEnums.Priority;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketFieldsEnums.ProgressStatus;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketFieldsEnums.Type;

import java.util.List;

public class TicketEditViewDto {
    private TicketForTicketEditViewDto ticket;
    private AppUserDto developer;
    private List<AppUserDto> possibleDevelopers;
    private Type[] types = Type.values();
    private Priority[] priorities = Priority.values();
    private ProgressStatus[] progressStatuses = ProgressStatus.values();

    public TicketEditViewDto(TicketForTicketEditViewDto ticket, AppUserDto developer, List<AppUserDto> possibleDevelopers) {
        this.ticket = ticket;
        this.developer = developer;
        this.possibleDevelopers = possibleDevelopers;
    }


    public TicketForTicketEditViewDto getTicket() {
        return ticket;
    }

    public void setTicket(TicketForTicketEditViewDto ticket) {
        this.ticket = ticket;
    }

    public AppUserDto getDeveloper() {
        return developer;
    }

    public void setDeveloper(AppUserDto developer) {
        this.developer = developer;
    }

    public List<AppUserDto> getPossibleDevelopers() {
        return possibleDevelopers;
    }

    public void setPossibleDevelopers(List<AppUserDto> possibleDevelopers) {
        this.possibleDevelopers = possibleDevelopers;
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

    public ProgressStatus[] getProgressStatuses() {
        return progressStatuses;
    }

    public void setProgressStatuses(ProgressStatus[] progressStatuses) {
        this.progressStatuses = progressStatuses;
    }
}
