package com.mcr.bugtracker.BugTrackerApplication.appuser;

import lombok.Getter;

import java.util.Objects;

@Getter
public final class UserProfileDto {
    private final String firstName;
    private final String lastName;
    private final String role;
    private final String email;
    private final int numberOfManagedProjects;
    private final int numberOfSubmittedTickets;
    private final int numberOfAssignedTickets;
    private final int numberOfBelongingProjects;

    public UserProfileDto(
            String firstName,
            String lastName,
            String role,
            String email,
            int numberOfManagedProjects,
            int numberOfSubmittedTickets,
            int numberOfAssignedTickets,
            int numberOfBelongingProjects
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.email = email;
        this.numberOfManagedProjects = numberOfManagedProjects;
        this.numberOfSubmittedTickets = numberOfSubmittedTickets;
        this.numberOfAssignedTickets = numberOfAssignedTickets;
        this.numberOfBelongingProjects = numberOfBelongingProjects;
    }

    public String firstName() {
        return firstName;
    }

    public String lastName() {
        return lastName;
    }

    public String role() {
        return role;
    }

    public String email() {
        return email;
    }

    public int numberOfManagedProjects() {
        return numberOfManagedProjects;
    }

    public int numberOfSubmittedTickets() {
        return numberOfSubmittedTickets;
    }

    public int numberOfAssignedTickets() {
        return numberOfAssignedTickets;
    }

    public int numberOfBelongingProjects() {
        return numberOfBelongingProjects;
    }

}
