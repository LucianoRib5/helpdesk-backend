package com.lucianoribeiro.helpdesk.service.exception;

public class ObjectInvalidPasswordException extends RuntimeException{

    public ObjectInvalidPasswordException(String message) {
        super(message);
    }
}
