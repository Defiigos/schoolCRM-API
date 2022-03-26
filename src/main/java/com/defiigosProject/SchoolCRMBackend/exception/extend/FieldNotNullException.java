package com.defiigosProject.SchoolCRMBackend.exception.extend;

import com.defiigosProject.SchoolCRMBackend.exception.HttpRequestException;
import org.springframework.http.HttpStatus;

public class FieldNotNullException extends HttpRequestException {
    public FieldNotNullException(String field) {
        super(field + " must not be empty!", HttpStatus.BAD_REQUEST);
    }
}
