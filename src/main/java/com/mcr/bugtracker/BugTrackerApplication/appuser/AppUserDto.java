package com.mcr.bugtracker.BugTrackerApplication.appuser;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public final class AppUserDto {
    private Long id;
    private String email;
    private String wholeName;
    private String sRole;

    public AppUserDto(Long id, String email, String wholeName, String sRole) {
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
}
