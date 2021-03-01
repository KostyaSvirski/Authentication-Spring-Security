package com.epam.esm.handler;

import com.epam.esm.dto.ActionHypermedia;
import com.epam.esm.exception.EntityIsAlreadyExistException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.IncorrectDataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


}
