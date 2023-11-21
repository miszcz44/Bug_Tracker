package com.mcr.bugtracker.BugTrackerApplication.appuser.Mapper;

import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import com.mcr.bugtracker.BugTrackerApplication.appuser.DTO.AppUserDto;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class AppUserDtoMapper implements Function<AppUser, AppUserDto> {
    @Override
    public AppUserDto apply(AppUser appUser) {
        return new AppUserDto(appUser.getId(),
                appUser.getEmail(),
                appUser.getWholeName(),
                appUser.getSRole());
    }
}
