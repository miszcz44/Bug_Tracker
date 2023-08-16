package com.mcr.bugtracker.BugTrackerApplication.Exceptions;

import org.springframework.http.HttpStatus;

public class ApiNotFoundException extends RuntimeException {
    public ApiNotFoundException(String message) {
        super(message);
    }

    public ApiNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
