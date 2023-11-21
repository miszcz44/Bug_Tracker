package com.mcr.bugtracker.BugTrackerApplication.appuser.Request;

public class PasswordChangeRequest {
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;

    public PasswordChangeRequest(String oldPassword, String newPassword, String confirmPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }
}
