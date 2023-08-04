package com.mcr.bugtracker.BugTrackerApplication.ticket;

import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import com.mcr.bugtracker.BugTrackerApplication.project.Project;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketFieldsEnums.Priority;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketFieldsEnums.ProgressStatus;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketFieldsEnums.Type;

import java.util.List;

public class TicketEditViewDto {
    private Ticket ticket;
    private String projectName;
    private AppUser developer;
    private List<AppUser> possibleDevelopers;
    private Type[] types = Type.values();
    private Priority[] priorities = Priority.values();
    private ProgressStatus[] progressStatuses = ProgressStatus.values();

    public TicketEditViewDto(Ticket ticket, String projectName, AppUser developer, List<AppUser> possibleDevelopers) {
        this.ticket = ticket;
        this.projectName = projectName;
        this.developer = developer;
        this.possibleDevelopers = possibleDevelopers;
    }


    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public AppUser getDeveloper() {
        return developer;
    }

    public void setDeveloper(AppUser developer) {
        this.developer = developer;
    }

    public List<AppUser> getPossibleDevelopers() {
        return possibleDevelopers;
    }

    public void setPossibleDevelopers(List<AppUser> possibleDevelopers) {
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
