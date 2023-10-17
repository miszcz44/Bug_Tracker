package com.mcr.bugtracker.BugTrackerApplication.ticket;

public class TicketForTicketEditViewDto {
    private Long id;
    private String title;
    private String description;
    private String priority;
    private String status;
    private String type;
    private String projectName;
    public TicketForTicketEditViewDto(Long id, String title, String description, String priority, String status, String type, String projectName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.type = type;
        this.projectName = projectName;
    }
    public Long getId() {
        return id;
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

    public String getProjectName() {
        return projectName;
    }
}
