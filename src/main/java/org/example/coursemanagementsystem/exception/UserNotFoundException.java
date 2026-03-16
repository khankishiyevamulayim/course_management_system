package org.example.coursemanagementsystem.exception;

import static org.example.coursemanagementsystem.util.Constants.USER_NOT_FOUND;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super(USER_NOT_FOUND);
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}

