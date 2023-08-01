package com.mcr.bugtracker.BugTrackerApplication.ticket;

import java.time.LocalDateTime;

public class AllTicketsViewDto {

    private Long id;
    private String title;
    private String projectName;
    private String developerName;
    private String priority;
    private String status;
    private String type;
    private LocalDateTime created;

    public AllTicketsViewDto(Long id, String title, String projectName, String developerName, String priority, String status, String type, LocalDateTime created) {
        this.id = id;
        this.title = title;
        this.projectName = projectName;
        this.developerName = developerName;
        this.priority = priority;
        this.status = status;
        this.type = type;
        this.created = created;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getDeveloperName() {
        return developerName;
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

    public LocalDateTime getCreated() {
        return created;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setDeveloperName(String developerName) {
        this.developerName = developerName;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
