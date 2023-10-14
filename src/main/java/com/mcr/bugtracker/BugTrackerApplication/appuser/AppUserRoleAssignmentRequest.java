package com.mcr.bugtracker.BugTrackerApplication.appuser;

import java.util.List;
public class AppUserRoleAssignmentRequest {
    private List<String> usersEmails;
    private String role;
    public AppUserRoleAssignmentRequest(List<String> usersEmails, String role) {
        this.usersEmails = usersEmails;
        this.role = role;
    }
    public List<String> getUsersEmails() {
        return usersEmails;
    }
    public void setUsersEmails(List<String> usersEmails) {
        this.usersEmails = usersEmails;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
}
