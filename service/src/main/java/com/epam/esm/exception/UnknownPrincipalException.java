package com.epam.esm.exception;

public class UnknownPrincipalException extends RuntimeException {

    public UnknownPrincipalException() {
    }

    public UnknownPrincipalException(String message) {
        super(message);
    }

    public UnknownPrincipalException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownPrincipalException(Throwable cause) {
        super(cause);
    }
}
