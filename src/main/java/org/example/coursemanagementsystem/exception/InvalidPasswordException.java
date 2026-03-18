package org.example.coursemanagementsystem.exception;

import static org.example.coursemanagementsystem.util.Constants.INVALID_PASSWORD;

public class InvalidPasswordException extends RuntimeException {

    public InvalidPasswordException(){
        super(INVALID_PASSWORD);
    }
    public InvalidPasswordException(String message) {
        super(message);
    }
}
