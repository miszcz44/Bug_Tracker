package com.mcr.bugtracker.BugTrackerApplication.ticket.ticketFieldsEnums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Priority {
    HIGH("High"),
    MEDIUM("Medium"),
    LOW("Low"),
    NONE("None");

    private final String name;

    Priority(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
