package com.mcr.bugtracker.BugTrackerApplication.ticket.commentary;

import java.time.LocalDateTime;

public class CommentsForTicketDetailsViewDto {

    private Long id;
    private String commentatorEmail;
    private String message;
    private LocalDateTime created;

    public CommentsForTicketDetailsViewDto(Long id, String commentatorEmail, String message, LocalDateTime created) {
        this.id = id;
        this.commentatorEmail = commentatorEmail;
        this.message = message;
        this.created = created;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommentatorEmail() {
        return commentatorEmail;
    }

    public void setCommentatorEmail(String commentatorEmail) {
        this.commentatorEmail = commentatorEmail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
}
