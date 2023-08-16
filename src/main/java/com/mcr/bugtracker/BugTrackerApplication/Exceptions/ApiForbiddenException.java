package com.mcr.bugtracker.BugTrackerApplication.Exceptions;

public class ApiForbiddenException extends RuntimeException {
    public ApiForbiddenException(String message) {
        super(message);
    }

    public ApiForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
}
