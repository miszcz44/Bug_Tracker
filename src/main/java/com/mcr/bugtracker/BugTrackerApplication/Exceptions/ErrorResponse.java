package com.mcr.bugtracker.BugTrackerApplication.Exceptions;

import org.springframework.http.HttpStatus;

public class ErrorResponse {

    private int statusCode;
    private String message;

    public ErrorResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
