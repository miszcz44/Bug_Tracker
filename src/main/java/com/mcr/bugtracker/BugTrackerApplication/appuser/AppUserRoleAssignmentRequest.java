package com.mcr.bugtracker.BugTrackerApplication.appuser;

import java.util.List;
public class AppUserRoleAssignmentRequest {
    List<String> usersEmails;
    String role;

    public AppUserRoleAssignmentRequest(List<String> usersEmails, String role) {
        this.usersEmails = usersEmails;
        this.role = role;
    }
}
