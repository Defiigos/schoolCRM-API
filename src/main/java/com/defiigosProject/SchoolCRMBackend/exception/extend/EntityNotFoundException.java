package com.defiigosProject.SchoolCRMBackend.exception.extend;

import com.defiigosProject.SchoolCRMBackend.exception.HttpRequestException;
import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends HttpRequestException {
    public EntityNotFoundException(String entity) {
        super(entity + " is not found!", HttpStatus.NOT_FOUND);
    }
}
