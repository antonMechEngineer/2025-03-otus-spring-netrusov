package ru.otus.hw.rest;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.hw.exceptions.BookingProcessException;
import ru.otus.hw.exceptions.EntityNotFoundException;

@SuppressWarnings("unused")
@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR_NOT_FOUND = "Information is not found!";

    private static final String ERROR_PROCESS_BOOKING = "Unfortunately room is already booked, try another room!";

    private static final String ERROR_REQUEST_LIMIT = "Too many requests. Please try again later.";

    private static final String ERROR_TEMPORARY_UNAVAILABLE = "Service temporarily unavailable. Please try again later.";

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handeNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.badRequest().body(ERROR_NOT_FOUND);
    }

    @ExceptionHandler(RequestNotPermitted.class)
    public ResponseEntity<String> handleRateLimiterException(RequestNotPermitted ex) {
        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body(ERROR_REQUEST_LIMIT);
    }

    @ExceptionHandler(CallNotPermittedException.class)
    public ResponseEntity<String> handleCircuitBreakerException(CallNotPermittedException ex) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ERROR_TEMPORARY_UNAVAILABLE);
    }

    @ExceptionHandler(BookingProcessException.class)
    public ResponseEntity<String> handleRoomAlreadyIsBooked(BookingProcessException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ERROR_PROCESS_BOOKING);
    }
}

