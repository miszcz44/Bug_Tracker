package com.mcr.bugtracker.BugTrackerApplication.appuser.Request;

public class EmailChangeRequest {
    private String oldEmail;
    private String newEmail;
    private String password;

    public EmailChangeRequest(String oldEmail, String newEmail, String password) {
        this.oldEmail = oldEmail;
        this.newEmail = newEmail;
        this.password = password;
    }

    public String getOldEmail() {
        return oldEmail;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public String getPassword() {
        return password;
    }
}
