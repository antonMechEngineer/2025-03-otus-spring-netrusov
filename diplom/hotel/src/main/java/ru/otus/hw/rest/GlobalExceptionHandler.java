package ru.otus.hw.rest;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.otus.hw.exceptions.BookingProcessException;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.rest.dto.ErrorDto;

import java.util.Arrays;

@SuppressWarnings("unused")
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR_UNEXPECTED_ERROR = "Server error!";

    private static final String ERROR_ENTITY_NOT_FOUND = "Entity is not found!";

    private static final String ERROR_USER_REQUEST = "Incorrect request, try your request params!";

    private static final String ERROR_ENDPOINT_NOT_FOUND = "Endpoint is not found!";

    private static final String ERROR_PROCESS_BOOKING = "Unfortunately room is already booked, try another room!";

    private static final String ERROR_REQUEST_LIMIT = "Too many requests. Please try again later.";

    private static final String ERROR_TEMPORARY_UNAVAILABLE = "Service temporarily unavailable. Please try again later.";

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleInternalServerErrorException(Exception ex) {
        log.error(Arrays.toString(ex.getStackTrace()));
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDto(ERROR_UNEXPECTED_ERROR, ex.getMessage()));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorDto> handleNotFoundException(NoHandlerFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorDto(ERROR_ENDPOINT_NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            MethodArgumentTypeMismatchException.class,
            MethodArgumentNotValidException.class
    })
    public ResponseEntity<ErrorDto> handleBadRequestException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDto(ERROR_USER_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDto> handeNotFoundException(EntityNotFoundException ex) {
        log.warn(Arrays.toString(ex.getStackTrace()));
        return ResponseEntity
                .badRequest()
                .body(new ErrorDto(ERROR_ENTITY_NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(RequestNotPermitted.class)
    public ResponseEntity<ErrorDto> handleRateLimiterException(RequestNotPermitted ex) {
        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body(new ErrorDto(ERROR_REQUEST_LIMIT, ex.getMessage()));
    }

    @ExceptionHandler(CallNotPermittedException.class)
    public ResponseEntity<ErrorDto> handleCircuitBreakerException(CallNotPermittedException ex) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new ErrorDto(ERROR_TEMPORARY_UNAVAILABLE, ex.getMessage()));
    }

    @ExceptionHandler(BookingProcessException.class)
    public ResponseEntity<ErrorDto> handleRoomAlreadyIsBooked(BookingProcessException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorDto(ERROR_PROCESS_BOOKING, ex.getMessage()));
    }
}