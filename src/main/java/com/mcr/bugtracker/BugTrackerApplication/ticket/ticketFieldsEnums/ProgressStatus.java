package com.mcr.bugtracker.BugTrackerApplication.ticket.ticketFieldsEnums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ProgressStatus {
    NEW("New"),
    OPEN("Open"),
    IN_PROGRESS("In progress"),
    RESOLVED("Resolved"),
    ADDITIONAL_INFO_REQUIRED("Additional info required");

    private final String name;

    ProgressStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
