package com.mcr.bugtracker.BugTrackerApplication.appuser;

import java.util.List;

public class AppUsersResponseDto {

    private List<AppUser> users;
    private AppUserRole[] userRoles = AppUserRole.values();

    public AppUsersResponseDto(List<AppUser> users) {
        this.users = users;
    }

    public List<AppUser> getUsers() {
        return users;
    }

    public void setUsers(List<AppUser> users) {
        this.users = users;
    }

    public AppUserRole[] getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(AppUserRole[] userRoles) {
        this.userRoles = userRoles;
    }
}
