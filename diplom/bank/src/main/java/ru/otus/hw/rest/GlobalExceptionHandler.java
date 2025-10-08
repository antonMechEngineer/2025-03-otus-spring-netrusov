package ru.otus.hw.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.rest.dto.ErrorDto;

import java.util.Arrays;

@SuppressWarnings("unused")
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR_UNEXPECTED_ERROR = "Server error!";

    private static final String ERROR_ENTITY_NOT_FOUND = "Entity is not found!";

    private static final String ERROR_USER_REQUEST = "Incorrect request, try your request params!";

    private static final String ERROR_ENDPOINT_NOT_FOUND = "Endpoint is not found!";

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleInternalServerErrorException(Exception ex) {
        log.error(Arrays.toString(ex.getStackTrace()));
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDto(ERROR_UNEXPECTED_ERROR, ex.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDto> handeNotFoundException(EntityNotFoundException ex) {
        log.warn(Arrays.toString(ex.getStackTrace()));
        return ResponseEntity
                .badRequest()
                .body(new ErrorDto(ERROR_ENTITY_NOT_FOUND, ex.getMessage()));
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
}
