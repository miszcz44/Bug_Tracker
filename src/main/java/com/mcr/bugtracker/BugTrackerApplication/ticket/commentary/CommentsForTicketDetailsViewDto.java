package com.mcr.bugtracker.BugTrackerApplication.ticket.commentary;

public class CommentsForTicketDetailsViewDto {

    private Long id;
    private String commentatorEmail;
    private String message;
    private String created;

    public CommentsForTicketDetailsViewDto(Long id, String commentatorEmail, String message, String created) {
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

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
