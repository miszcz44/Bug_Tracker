package com.mcr.bugtracker.BugTrackerApplication.ticket;


import java.time.LocalDateTime;

public class TicketForProjectViewDto {

    private String title;
    private String submitter;
    private String developer;
    private String status;
    private LocalDateTime created;

    public TicketForProjectViewDto(String title, String submitter, String developer, String status, LocalDateTime created) {
        this.title = title;
        this.submitter = submitter;
        this.developer = developer;
        this.status = status;
        this.created = created;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubmitter() {
        return submitter;
    }

    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
}
