package com.mcr.bugtracker.BugTrackerApplication.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiRequestException.class)
    public ResponseEntity<Object> handleApiRequestException(ApiRequestException e) {
        ApiException apiException = new ApiException(
                e.getMessage(),
                e,
                HttpStatus.BAD_REQUEST
        );
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApiForbiddenException.class)
    public ResponseEntity<Object> handleNotAuthorizedException(ApiForbiddenException e) {
        ApiException apiException = new ApiException(
                e.getMessage(),
                e,
                HttpStatus.FORBIDDEN
        );
        return new ResponseEntity<>(apiException, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ApiNotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(ApiNotFoundException e) {
        ApiException apiException = new ApiException(
                e.getMessage(),
                e,
                HttpStatus.NOT_FOUND
        );
        return new ResponseEntity<>(apiException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ApiPasswordDoesntMatchException.class)
    public ResponseEntity<Object> handlePasswordDoesntMatchException(ApiPasswordDoesntMatchException e) {
        ApiException apiException = new ApiException(
                e.getMessage(),
                e,
                HttpStatus.FORBIDDEN
        );
        return new ResponseEntity<>(apiException, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ApiEmailTakenException.class)
    public ResponseEntity<Object> handleEmailTakenException(ApiEmailTakenException e) {
        ApiException apiException = new ApiException(
                e.getMessage(),
                e,
                HttpStatus.CONFLICT
        );
        return new ResponseEntity<>(apiException, HttpStatus.CONFLICT);
    }

}
