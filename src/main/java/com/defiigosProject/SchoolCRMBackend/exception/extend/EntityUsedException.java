package com.defiigosProject.SchoolCRMBackend.exception.extend;

public class EntityUsedException extends Exception{
    public EntityUsedException(String entity, String user) {
        super(entity + " is used by " + user + "!");
    }
}
