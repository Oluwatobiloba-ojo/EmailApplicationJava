package org.example.exceptions;

public class EmailApplicationException extends RuntimeException{
    public EmailApplicationException(String message){
        super(message);
    }
}
