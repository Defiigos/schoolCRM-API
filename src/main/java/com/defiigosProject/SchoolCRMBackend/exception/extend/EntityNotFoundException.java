package com.defiigosProject.SchoolCRMBackend.exception.extend;

public class EntityNotFoundException extends Exception {
    public EntityNotFoundException(String entity) {
        super(entity + " is not found!");
    }
}
