package ru.otus.hw.rest;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.hw.exceptions.EntityNotFoundException;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {

    public static final String ERROR_STRING = "Information not found!";

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handeNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(404).body(ERROR_STRING);
    }

    @ExceptionHandler(RequestNotPermitted.class)
    public ResponseEntity<String> handleRateLimiterException(RequestNotPermitted ex) {
        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body("Too many requests. Please try again later.");
    }

    @ExceptionHandler(CallNotPermittedException.class)
    public ResponseEntity<String> handleRateLimiterException(CallNotPermittedException ex) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Service temporarily unavailable. Please try again later.");
    }

}
