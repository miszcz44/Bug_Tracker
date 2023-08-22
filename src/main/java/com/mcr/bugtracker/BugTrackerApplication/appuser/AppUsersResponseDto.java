package com.mcr.bugtracker.BugTrackerApplication.appuser;

import java.util.List;

public class AppUsersResponseDto {

    private List<AppUser> allUsers;
    private List<AppUser> nonAdminUsersEmails;
    private AppUserRole[] userRoles;

    public AppUsersResponseDto(List<AppUser> allUsers, List<AppUser> nonAdminUsersEmails, AppUserRole[] userRoles) {
        this.allUsers = allUsers;
        this.nonAdminUsersEmails = nonAdminUsersEmails;
        this.userRoles = userRoles;
    }

    public List<AppUser> getAllUsers() {
        return allUsers;
    }

    public void setAllUsers(List<AppUser> allUsers) {
        this.allUsers = allUsers;
    }

    public List<AppUser> getNonAdminUsersEmails() {
        return nonAdminUsersEmails;
    }

    public void setNonAdminUsersEmails(List<AppUser> nonAdminUsersEmails) {
        this.nonAdminUsersEmails = nonAdminUsersEmails;
    }

    public AppUserRole[] getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(AppUserRole[] userRoles) {
        this.userRoles = userRoles;
    }
}
