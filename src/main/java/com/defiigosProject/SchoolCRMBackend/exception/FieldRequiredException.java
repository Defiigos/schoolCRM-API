package com.defiigosProject.SchoolCRMBackend.exception;

public class FieldRequiredException extends Exception {
    public FieldRequiredException(String field) {
        super("'" + field + "' required!");
    }
}
