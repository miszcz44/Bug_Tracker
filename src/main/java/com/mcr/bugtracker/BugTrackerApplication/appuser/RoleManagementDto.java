package com.mcr.bugtracker.BugTrackerApplication.appuser;

import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class RoleManagementDto {

    private List<AppUserDto> allUsers;
    private List<String> nonAdminUsersEmails;
    private AppUserRole[] userRoles;

    public RoleManagementDto(List<AppUserDto> allUsers, List<String> nonAdminUsersEmails, AppUserRole[] userRoles) {
        this.allUsers = allUsers;
        this.nonAdminUsersEmails = nonAdminUsersEmails;
        this.userRoles = userRoles;
    }
    public List<AppUserDto> getAllUsers() {
        return allUsers;
    }
    public List<String> getNonAdminUsersEmails() {
        return nonAdminUsersEmails;
    }
}
