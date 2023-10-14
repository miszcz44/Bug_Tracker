package com.mcr.bugtracker.BugTrackerApplication.appuser;

import java.util.List;

public class RoleManagementDto {

    private List<AppUserForRoleManagementDto> allUsers;
    private List<String> nonAdminUsersEmails;
    private AppUserRole[] userRoles;

    public RoleManagementDto(List<AppUserForRoleManagementDto> allUsers, List<String> nonAdminUsersEmails, AppUserRole[] userRoles) {
        this.allUsers = allUsers;
        this.nonAdminUsersEmails = nonAdminUsersEmails;
        this.userRoles = userRoles;
    }

    public List<AppUserForRoleManagementDto> getAllUsers() {
        return allUsers;
    }

    public void setAllUsers(List<AppUserForRoleManagementDto> allUsers) {
        this.allUsers = allUsers;
    }

    public List<String> getNonAdminUsersEmails() {
        return nonAdminUsersEmails;
    }

    public void setNonAdminUsersEmails(List<String> nonAdminUsersEmails) {
        this.nonAdminUsersEmails = nonAdminUsersEmails;
    }

    public AppUserRole[] getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(AppUserRole[] userRoles) {
        this.userRoles = userRoles;
    }
}
