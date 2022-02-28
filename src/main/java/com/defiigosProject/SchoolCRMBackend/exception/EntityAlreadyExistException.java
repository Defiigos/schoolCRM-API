package com.defiigosProject.SchoolCRMBackend.exception;

public class EntityAlreadyExistException extends Exception {
    public EntityAlreadyExistException(String entity) {
        super("This " + entity + " already exist!");
    }
}
