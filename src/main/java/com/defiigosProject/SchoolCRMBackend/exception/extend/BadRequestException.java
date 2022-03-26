package com.defiigosProject.SchoolCRMBackend.exception.extend;

import com.defiigosProject.SchoolCRMBackend.exception.HttpRequestException;
import org.springframework.http.HttpStatus;

public class BadRequestException extends HttpRequestException {
    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
