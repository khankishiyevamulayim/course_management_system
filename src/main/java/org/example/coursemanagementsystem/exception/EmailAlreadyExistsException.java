package org.example.coursemanagementsystem.exception;

import static org.example.coursemanagementsystem.util.Constants.EMAIL_ALREADY_EXISTS;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException() {
        super( EMAIL_ALREADY_EXISTS);
    }
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
