package com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistoryField;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PropertyEnum {
    TITLE("Title"),
    DESCRIPTION("Description"),
    DEVELOPER("Developer"),
    PRIORITY("Priority"),
    TYPE("Type"),
    STATUS("Status");
    private final String name;
    PropertyEnum(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
