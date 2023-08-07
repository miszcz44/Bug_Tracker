package com.mcr.bugtracker.BugTrackerApplication.appuser;

public class EmailChangeResponse {
    private String oldEmail;
    private String newEmail;
    private String password;

    public EmailChangeResponse(String oldEmail, String newEmail, String password) {
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
