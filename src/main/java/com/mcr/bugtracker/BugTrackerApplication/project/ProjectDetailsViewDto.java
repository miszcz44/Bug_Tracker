package com.mcr.bugtracker.BugTrackerApplication.project;

import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUserDto;
import com.mcr.bugtracker.BugTrackerApplication.ticket.TicketForProjectViewDto;

import java.util.List;

public class ProjectDetailsViewDto {
    private ProjectDto project;
    private String projectManagerName;
    private String projectManagerEmail;
    private List<AppUserDto> projectPersonnel;
    private List<TicketForProjectViewDto> tickets;

    public ProjectDetailsViewDto(ProjectDto project, String projectManagerName, String projectManagerEmail, List<AppUserDto> projectPersonnel, List<TicketForProjectViewDto> tickets) {
        this.project = project;
        this.projectManagerName = projectManagerName;
        this.projectManagerEmail = projectManagerEmail;
        this.projectPersonnel = projectPersonnel;
        this.tickets = tickets;
    }

    public ProjectDto getProject() {
        return project;
    }

    public void setProject(ProjectDto project) {
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

    public List<AppUserDto> getProjectPersonnel() {
        return projectPersonnel;
    }

    public void setProjectPersonnel(List<AppUserDto> projectPersonnel) {
        this.projectPersonnel = projectPersonnel;
    }

    public List<TicketForProjectViewDto> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketForProjectViewDto> tickets) {
        this.tickets = tickets;
    }
}
