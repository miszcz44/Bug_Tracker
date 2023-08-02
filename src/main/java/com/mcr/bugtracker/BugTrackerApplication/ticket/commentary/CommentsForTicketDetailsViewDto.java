package com.mcr.bugtracker.BugTrackerApplication.ticket.commentary;

public class CommentsForTicketDetailsViewDto {

    private String commentatorName;
    private Commentary commentary;

    public CommentsForTicketDetailsViewDto(String commentatorName, Commentary commentary) {
        this.commentatorName = commentatorName;
        this.commentary = commentary;
    }

    public String getCommentatorName() {
        return commentatorName;
    }

    public void setCommentatorName(String commentatorName) {
        this.commentatorName = commentatorName;
    }

    public Commentary getCommentary() {
        return commentary;
    }

    public void setCommentary(Commentary commentary) {
        this.commentary = commentary;
    }
}
