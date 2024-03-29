package com.mcr.bugtracker.BugTrackerApplication.Exceptions;

import org.springframework.http.HttpStatus;

import java.util.Objects;

public class ApiException {
    private final String message;
    private final Throwable throwable;
    private final HttpStatus httpStatus;

    public ApiException(String message, Throwable throwable, HttpStatus httpStatus) {
        this.message = message;
        this.throwable = throwable;
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
