package com.mcr.bugtracker.BugTrackerApplication.ticket;

import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import com.mcr.bugtracker.BugTrackerApplication.ticket.attachment.Attachment;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketFieldsEnums.Priority;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketFieldsEnums.ProgressStatus;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketFieldsEnums.Type;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistoryField.TicketHistoryField;

import java.util.List;

public class TicketResponseDto {

    private Ticket ticket;
    private Type[] types = Type.values();
    private Priority[] priorities = Priority.values();
    private ProgressStatus[] progressStatuses = ProgressStatus.values();
    private List<AppUser> projectPersonnel;
    private AppUser developer;
    private List<TicketHistoryField> historyFields;
    private List<Attachment> attachments;

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public List<TicketHistoryField> getHistoryFields() {
        return historyFields;
    }

    public void setHistoryFields(List<TicketHistoryField> historyFields) {
        this.historyFields = historyFields;
    }

    public AppUser getDeveloper() {
        return developer;
    }

    public void setDeveloper(AppUser developer) {
        this.developer = developer;
    }

    public TicketResponseDto(Ticket ticket, List<AppUser> projectPersonnel, List<TicketHistoryField> historyFields, List<Attachment> attachments) {
        this.ticket = ticket;
        this.projectPersonnel = projectPersonnel;
        this.historyFields = historyFields;
        this.attachments = attachments;
    }

    public void setProgressStatuses(ProgressStatus[] progressStatuses) {
        this.progressStatuses = progressStatuses;
    }

    public TicketResponseDto(Ticket ticket) {
        this.ticket = ticket;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Type[] getTypes() {
        return types;
    }

    public void setTypes(Type[] types) {
        this.types = types;
    }

    public Priority[] getPriorities() {
        return priorities;
    }

    public void setPriorities(Priority[] priorities) {
        this.priorities = priorities;
    }

    public ProgressStatus[] getProgressStatuses() {
        return progressStatuses;
    }

    public List<AppUser> getProjectPersonnel() {
        return projectPersonnel;
    }

    public void setProjectPersonnel(List<AppUser> projectPersonnel) {
        this.projectPersonnel = projectPersonnel;
    }
}
