package com.mcr.bugtracker.BugTrackerApplication.ticket;

import java.time.LocalDateTime;

public class TicketForTicketDetailsViewDto {
    private String title;
    private String description;
    private String priority;
    private String status;
    private String type;
    private LocalDateTime createdAt;
    private String developerName;
    private String submitterName;
    private String submitterEmail;
    private Long projectId;
    private String projectName;
    private String projectManagerEmail;

    public TicketForTicketDetailsViewDto(String title, String description, String priority, String status, String type, LocalDateTime createdAt, String developerName, String submitterName, String submitterEmail, Long projectId, String projectName, String projectManagerEmail) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.type = type;
        this.createdAt = createdAt;
        this.developerName = developerName;
        this.submitterName = submitterName;
        this.submitterEmail = submitterEmail;
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectManagerEmail = projectManagerEmail;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPriority() {
        return priority;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getDeveloperName() {
        return developerName;
    }

    public String getSubmitterName() {
        return submitterName;
    }

    public String getSubmitterEmail() {
        return submitterEmail;
    }

    public Long getProjectId() {
        return projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getProjectManagerEmail() {
        return projectManagerEmail;
    }
}
