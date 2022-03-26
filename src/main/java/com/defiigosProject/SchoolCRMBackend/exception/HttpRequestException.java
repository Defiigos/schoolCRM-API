package com.defiigosProject.SchoolCRMBackend.exception;

import org.springframework.http.HttpStatus;

public class HttpRequestException extends Exception {
    private final HttpStatus httpStatus;

    public HttpRequestException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
