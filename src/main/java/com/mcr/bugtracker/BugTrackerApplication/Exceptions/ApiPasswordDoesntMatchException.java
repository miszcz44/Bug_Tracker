package com.mcr.bugtracker.BugTrackerApplication.Exceptions;

public class ApiPasswordDoesntMatchException extends RuntimeException {
    public ApiPasswordDoesntMatchException(String message) {
        super(message);
    }

    public ApiPasswordDoesntMatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
