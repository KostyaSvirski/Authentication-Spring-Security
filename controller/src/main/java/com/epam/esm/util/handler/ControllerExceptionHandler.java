package com.epam.esm.util.handler;

import com.epam.esm.dto.ActionHypermedia;
import com.epam.esm.exception.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ActionHypermedia> handleException(IllegalArgumentException e) {
        ActionHypermedia hypermedia = new ActionHypermedia(e.getMessage());
        return new ResponseEntity<>(hypermedia, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ActionHypermedia> handleException(EntityNotFoundException e) {
        ActionHypermedia hypermedia = new ActionHypermedia(e.getMessage());
        return new ResponseEntity<>(hypermedia, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IncorrectDataException.class)
    public ResponseEntity<ActionHypermedia> handleException(IncorrectDataException e) {
        ActionHypermedia hypermedia = new ActionHypermedia(e.getMessage());
        return new ResponseEntity<>(hypermedia, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityIsAlreadyExistException.class)
    public ResponseEntity<ActionHypermedia> handleException(EntityIsAlreadyExistException e) {
        ActionHypermedia hypermedia = new ActionHypermedia(e.getMessage());
        return new ResponseEntity<>(hypermedia, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AuthenticationServiceException.class)
    public ResponseEntity<ActionHypermedia> handleException(AuthenticationServiceException e) {
        ActionHypermedia hypermedia = new ActionHypermedia(e.getMessage());
        return new ResponseEntity<>(hypermedia, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UnknownPrincipalException.class)
    public ResponseEntity<ActionHypermedia> handleException(UnknownPrincipalException e) {
        ActionHypermedia hypermedia = new ActionHypermedia(e.getMessage());
        return new ResponseEntity<>(hypermedia, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ActionHypermedia> handleException(ExpiredJwtException e) {
        ActionHypermedia hypermedia = new ActionHypermedia("Token expired");
        return new ResponseEntity<>(hypermedia, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity<ActionHypermedia> handleException(UnsupportedJwtException e) {
        ActionHypermedia hypermedia = new ActionHypermedia("Unsupported jwt");
        return new ResponseEntity<>(hypermedia, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ActionHypermedia> handleException(MalformedJwtException e) {
        ActionHypermedia hypermedia = new ActionHypermedia("Malformed jwt");
        return new ResponseEntity<>(hypermedia, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ActionHypermedia> handleException(SignatureException e) {
        ActionHypermedia hypermedia = new ActionHypermedia("Invalid signature");
        return new ResponseEntity<>(hypermedia, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotValidDataForAuthenticateException.class)
    public ResponseEntity<ActionHypermedia> handleException(NotValidDataForAuthenticateException e) {
        ActionHypermedia hypermedia = new ActionHypermedia(e.getMessage());
        return new ResponseEntity<>(hypermedia, HttpStatus.BAD_REQUEST);
    }


}
