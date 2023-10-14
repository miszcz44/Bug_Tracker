package com.mcr.bugtracker.BugTrackerApplication.appuser;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AppUserRole {
    ADMIN("Admin"),
    PROJECT_MANAGER("Project manager"),
    DEVELOPER("Developer"),
    SUBMITTER("Submitter"),
    DEMO_ADMIN("Demo admin"),
    DEMO_PROJECT_MANAGER("Demo project manager"),
    DEMO_DEVELOPER("Demo developer"),
    DEMO_SUBMITTER("Demo submitter"),
    NONE("None");
    private final String name;
    AppUserRole(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

}
