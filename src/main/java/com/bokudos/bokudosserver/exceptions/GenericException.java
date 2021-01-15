package com.bokudos.bokudosserver.exceptions;

public abstract class GenericException extends RuntimeException {
    public GenericException(String message) {
        super(message);
    }
}
