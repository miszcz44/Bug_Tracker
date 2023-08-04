package com.mcr.bugtracker.BugTrackerApplication.ticket;

import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import com.mcr.bugtracker.BugTrackerApplication.project.Project;
import java.util.List;

public class TicketEditViewDto {
    private Ticket ticket;
    private String projectName;
    private AppUser developer;
    List<AppUser> possibleDevelopers;

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
}
