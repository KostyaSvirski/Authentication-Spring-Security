package com.epam.esm.exception;

public class PasswordIncorrectException extends RuntimeException {

    public PasswordIncorrectException() {
    }

    public PasswordIncorrectException(String message) {
        super(message);
    }

    public PasswordIncorrectException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordIncorrectException(Throwable cause) {
        super(cause);
    }
}
