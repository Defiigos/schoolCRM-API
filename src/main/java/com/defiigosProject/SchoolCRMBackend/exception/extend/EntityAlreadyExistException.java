package com.defiigosProject.SchoolCRMBackend.exception.extend;

import com.defiigosProject.SchoolCRMBackend.exception.HttpRequestException;
import org.springframework.http.HttpStatus;

public class EntityAlreadyExistException extends HttpRequestException {
    public EntityAlreadyExistException(String entity) {
        super("This " + entity + " already exist!", HttpStatus.NOT_ACCEPTABLE);
    }
}
