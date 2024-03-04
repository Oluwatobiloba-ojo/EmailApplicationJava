package org.example.exceptions;

public class InvalidLoginDetail extends EmailApplicationException {
    public InvalidLoginDetail(String message) {
        super(message);
    }
}
