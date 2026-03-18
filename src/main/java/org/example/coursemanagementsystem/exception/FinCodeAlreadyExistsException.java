package org.example.coursemanagementsystem.exception;

import static org.example.coursemanagementsystem.util.Constants.FIN_ALREADY_EXISTS;

public class FinCodeAlreadyExistsException extends RuntimeException {
    public FinCodeAlreadyExistsException(){
        super(FIN_ALREADY_EXISTS);
    }
    public FinCodeAlreadyExistsException(String message) {
        super(message);
    }
}
