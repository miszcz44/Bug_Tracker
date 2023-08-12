package com.mcr.bugtracker.BugTrackerApplication.ticket;

import com.mcr.bugtracker.BugTrackerApplication.project.Project;
import com.mcr.bugtracker.BugTrackerApplication.ticket.commentary.CommentsForTicketDetailsViewDto;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistoryField.TicketHistoryField;
import java.util.List;

public class TicketDetailsViewDto {
    private Ticket ticket;
    private String developerName;
    private String submitterName;
    private String submitterEmail;
    private Long projectId;
    private String projectName;
    private String projectManagerEmail;
    private List<CommentsForTicketDetailsViewDto> comments;
    private List<TicketHistoryField> ticketHistoryField;

    public TicketDetailsViewDto(Ticket ticket, String developerName, String submitterName, String submitterEmail, Long projectId, String projectName, String projectManagerEmail, List<CommentsForTicketDetailsViewDto> comments, List<TicketHistoryField> ticketHistoryField) {
        this.ticket = ticket;
        this.developerName = developerName;
        this.submitterName = submitterName;
        this.submitterEmail = submitterEmail;
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectManagerEmail = projectManagerEmail;
        this.comments = comments;
        this.ticketHistoryField = ticketHistoryField;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public String getDeveloperName() {
        return developerName;
    }

    public void setDeveloperName(String developerName) {
        this.developerName = developerName;
    }

    public String getSubmitterName() {
        return submitterName;
    }

    public void setSubmitterName(String submitterName) {
        this.submitterName = submitterName;
    }

    public String getSubmitterEmail() {
        return submitterEmail;
    }

    public void setSubmitterEmail(String submitterEmail) {
        this.submitterEmail = submitterEmail;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectManagerEmail() {
        return projectManagerEmail;
    }

    public void setProjectManagerEmail(String projectManagerEmail) {
        this.projectManagerEmail = projectManagerEmail;
    }

    public List<CommentsForTicketDetailsViewDto> getComments() {
        return comments;
    }

    public void setComments(List<CommentsForTicketDetailsViewDto> comments) {
        this.comments = comments;
    }

    public List<TicketHistoryField> getTicketHistoryField() {
        return ticketHistoryField;
    }

    public void setTicketHistoryField(List<TicketHistoryField> ticketHistoryField) {
        this.ticketHistoryField = ticketHistoryField;
    }
}
