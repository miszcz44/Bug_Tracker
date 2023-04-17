package com.mcr.bugtracker.BugTrackerApplication.registration;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class AuthCredentialsRequest {
    private String email;
    private String password;
}
