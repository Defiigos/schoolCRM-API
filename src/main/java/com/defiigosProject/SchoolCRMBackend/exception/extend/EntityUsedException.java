package com.defiigosProject.SchoolCRMBackend.exception.extend;

import com.defiigosProject.SchoolCRMBackend.exception.HttpRequestException;
import org.springframework.http.HttpStatus;

public class EntityUsedException extends HttpRequestException {
    public EntityUsedException(String entity, String user) {
        super(entity + " is used by " + user + "!", HttpStatus.NOT_ACCEPTABLE);
    }
}
