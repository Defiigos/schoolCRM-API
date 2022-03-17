package com.defiigosProject.SchoolCRMBackend.exception.extend;

public class FieldNotNullException extends Exception {
    public FieldNotNullException(String field) {
        super(field + " must not be empty!");
    }
}
