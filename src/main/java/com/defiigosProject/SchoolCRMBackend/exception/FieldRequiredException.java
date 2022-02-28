package com.defiigosProject.SchoolCRMBackend.exception;

import java.lang.reflect.Field;

public class FieldRequiredException extends Exception {
    public FieldRequiredException(String field) {
        super("'" + field + "' required!");
    }
}
