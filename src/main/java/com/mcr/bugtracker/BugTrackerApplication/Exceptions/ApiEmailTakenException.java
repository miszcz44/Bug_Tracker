package com.mcr.bugtracker.BugTrackerApplication.Exceptions;

public class ApiEmailTakenException extends RuntimeException {
    public ApiEmailTakenException(String message) {
        super(message);
    }

    public ApiEmailTakenException(String message, Throwable cause) {
        super(message, cause);
    }
}
