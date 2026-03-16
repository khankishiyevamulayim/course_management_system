package org.example.coursemanagementsystem.exception;

import static org.example.coursemanagementsystem.util.Constants.ACCOUNT_DISABLED;

public class AccountDisabledException extends RuntimeException {
    public AccountDisabledException() {
        super(ACCOUNT_DISABLED);
    }
    public AccountDisabledException(String message) {
        super(message);
    }
}
