package com.mcr.bugtracker.BugTrackerApplication.project;

import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import com.mcr.bugtracker.BugTrackerApplication.ticket.TicketForProjectViewDto;

import java.util.List;

public class ProjectDetailsViewDto {
    private Project project;
    private String projectManagerName;
    private String projectManagerEmail;
    private List<AppUser> projectPersonnel;
    private List<TicketForProjectViewDto> tickets;

    public ProjectDetailsViewDto(Project project, String projectManagerName, String projectManagerEmail, List<AppUser> projectPersonnel, List<TicketForProjectViewDto> tickets) {
        this.project = project;
        this.projectManagerName = projectManagerName;
        this.projectManagerEmail = projectManagerEmail;
        this.projectPersonnel = projectPersonnel;
        this.tickets = tickets;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getProjectManagerName() {
        return projectManagerName;
    }

    public void setProjectManagerName(String projectManagerName) {
        this.projectManagerName = projectManagerName;
    }

    public String getProjectManagerEmail() {
        return projectManagerEmail;
    }

    public void setProjectManagerEmail(String projectManagerEmail) {
        this.projectManagerEmail = projectManagerEmail;
    }

    public List<AppUser> getProjectPersonnel() {
        return projectPersonnel;
    }

    public void setProjectPersonnel(List<AppUser> projectPersonnel) {
        this.projectPersonnel = projectPersonnel;
    }

    public List<TicketForProjectViewDto> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketForProjectViewDto> tickets) {
        this.tickets = tickets;
    }
}
