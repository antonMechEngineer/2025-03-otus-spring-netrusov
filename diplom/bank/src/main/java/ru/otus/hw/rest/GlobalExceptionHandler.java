package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.hw.exceptions.EntityNotFoundException;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {

    public static final String ERROR_STRING = "Information is not found!";

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handeNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(404).body(ERROR_STRING);
    }

}
