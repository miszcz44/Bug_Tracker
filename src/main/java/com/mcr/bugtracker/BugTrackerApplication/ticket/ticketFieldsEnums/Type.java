package com.mcr.bugtracker.BugTrackerApplication.ticket.ticketFieldsEnums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Type {
    BUGS__ERRORS("Bugs/Errors"), // BUGS/ERRORS
    FEATURE_REQUESTS("Feature requests"),
    OTHER_COMMENTS("Other comments"),
    TRAINING__DOCUMENT_REQUESTS("Training/Document requests"); // TRAINING/DOCUMENT REQUESTS
    private final String name;
    Type(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
