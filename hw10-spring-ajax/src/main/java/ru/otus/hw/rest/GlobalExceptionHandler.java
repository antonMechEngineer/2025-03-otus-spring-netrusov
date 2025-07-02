package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.hw.rest.exceptions.NotFoundException;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {

    public static final String ERROR_STRING = "Информация не найдена";

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handeNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(404).body(ERROR_STRING);
    }

}
