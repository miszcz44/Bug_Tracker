package com.mcr.bugtracker.BugTrackerApplication.appuser;

import lombok.Getter;

@Getter
public final class AppUserForRoleManagementDto {
    private Long id;
    private String email;
    private String wholeName;
    private String sRole;

    public AppUserForRoleManagementDto(Long id, String email, String wholeName, String sRole) {
        this.id = id;
        this.email = email;
        this.wholeName = wholeName;
        this.sRole = sRole;
    }
    public Long id() {
        return id;
    }
    public String email() {
        return email;
    }
    public String wholeName() {
        return wholeName;
    }
    public String sRole() {
        return sRole;
    }
}
