package com.mcr.bugtracker.BugTrackerApplication.appuser;

public class AppUserResponseDto {

    private AppUser user;
    private AppUserRole role;

    public AppUserResponseDto(AppUser user, AppUserRole role) {
        this.user = user;
        this.role = role;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public AppUserRole getRole() {
        return role;
    }

    public void setRole(AppUserRole role) {
        this.role = role;
    }
}
