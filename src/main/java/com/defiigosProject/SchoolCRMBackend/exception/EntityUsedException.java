package com.defiigosProject.SchoolCRMBackend.exception;

public class EntityUsedException extends Exception{
    public EntityUsedException(String entity, String user) {
        super(entity + " is used by " + user + "!");
    }
}
