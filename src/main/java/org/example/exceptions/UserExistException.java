package org.example.exceptions;

public class UserExistException extends EmailApplicationException {
    public UserExistException(String message) {
        super(message);
    }
}
