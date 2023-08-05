package com.mcr.bugtracker.BugTrackerApplication.appuser;

import lombok.Getter;

@Getter
public record UserProfileDto (
        String firstName,
        String lastName,
        String role,
        String email,
        int numberOfManagedProjects,
        int numberOfSubmittedTickets
) {

}
