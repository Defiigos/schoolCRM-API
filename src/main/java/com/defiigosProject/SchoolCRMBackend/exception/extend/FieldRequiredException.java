package com.defiigosProject.SchoolCRMBackend.exception.extend;

import com.defiigosProject.SchoolCRMBackend.exception.HttpRequestException;
import org.springframework.http.HttpStatus;

public class FieldRequiredException extends HttpRequestException {
    public FieldRequiredException(String field) {
        super("'" + field + "' required!", HttpStatus.BAD_REQUEST);
    }
}
