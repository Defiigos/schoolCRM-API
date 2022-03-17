package com.defiigosProject.SchoolCRMBackend.exception.extend;

public class FieldRequiredException extends Exception {
    public FieldRequiredException(String field) {
        super("'" + field + "' required!");
    }
}
