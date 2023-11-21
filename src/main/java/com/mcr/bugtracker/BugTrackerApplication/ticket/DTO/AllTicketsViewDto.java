package com.mcr.bugtracker.BugTrackerApplication.ticket.DTO;

import java.time.LocalDateTime;

public class AllTicketsViewDto {

    private Long id;
    private String title;
    private String projectName;
    private String projectManagerEmail;
    private String submitterEmail;
    private String developerName;
    private String priority;
    private String status;
    private String type;
    private LocalDateTime created;

    public AllTicketsViewDto(Long id, String title, String projectName, String projectManagerEmail, String submitterEmail, String developerName, String priority, String status, String type, LocalDateTime created) {
        this.id = id;
        this.title = title;
        this.projectName = projectName;
        this.projectManagerEmail = projectManagerEmail;
        this.submitterEmail = submitterEmail;
        this.developerName = developerName;
        this.priority = priority;
        this.status = status;
        this.type = type;
        this.created = created;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getSubmitterEmail() {
        return submitterEmail;
    }

    public void setSubmitterEmail(String submitterEmail) {
        this.submitterEmail = submitterEmail;
    }

    public String getDeveloperName() {
        return developerName;
    }

    public void setDeveloperName(String developerName) {
        this.developerName = developerName;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
}
