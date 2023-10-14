package com.mcr.bugtracker.BugTrackerApplication.appuser;

import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class AppUserForRoleManagementMapper implements Function<AppUser, AppUserForRoleManagementDto> {
    @Override
    public AppUserForRoleManagementDto apply(AppUser appUser) {
        return new AppUserForRoleManagementDto(appUser.getId(),
                appUser.getEmail(),
                appUser.getWholeName(),
                appUser.getSRole());
    }
}
