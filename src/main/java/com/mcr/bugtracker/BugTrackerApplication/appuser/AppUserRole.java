package com.mcr.bugtracker.BugTrackerApplication.appuser;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AppUserRole {
    ADMIN("Admin"),
    PROJECT_MANAGER("Project manager"),
    DEVELOPER("Developer"),
    SUBMITTER("Submitter");

    private String name;

    AppUserRole(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
