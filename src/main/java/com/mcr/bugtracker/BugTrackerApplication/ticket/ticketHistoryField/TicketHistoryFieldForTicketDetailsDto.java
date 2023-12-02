package com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistoryField;

public class TicketHistoryFieldForTicketDetailsDto {
    private String property;
    private String oldValue;
    private String newValue;
    private String dateChanged;

    public TicketHistoryFieldForTicketDetailsDto(String property, String oldValue, String newValue, String dateChanged) {
        this.property = property;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.dateChanged = dateChanged;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getDateChanged() {
        return dateChanged;
    }

    public void setDateChanged(String dateChanged) {
        this.dateChanged = dateChanged;
    }
}
