package com.mcr.bugtracker.BugTrackerApplication.appuser.Mapper;

import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import com.mcr.bugtracker.BugTrackerApplication.appuser.DTO.UserProfileDto;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserProfileDtoMapper implements Function<AppUser, UserProfileDto> {
    @Override
    public UserProfileDto apply(AppUser appUser) {
        return new UserProfileDto(appUser.getFirstName(),
                appUser.getLastName(),
                appUser.getAppUserRole().getName(),
                appUser.getEmail(),
                appUser.getManagedProjects().size(),
                appUser.getSubmittedTickets().size(),
                appUser.getAssignedTickets().size(),
                appUser.getAssignedProjects().size()
        );
    }
}
