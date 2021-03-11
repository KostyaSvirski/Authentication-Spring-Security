package com.epam.esm.exception;

public class NotValidDataForAuthenticateException extends RuntimeException{

    public NotValidDataForAuthenticateException() {
    }

    public NotValidDataForAuthenticateException(String message) {
        super(message);
    }

    public NotValidDataForAuthenticateException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotValidDataForAuthenticateException(Throwable cause) {
        super(cause);
    }
}
