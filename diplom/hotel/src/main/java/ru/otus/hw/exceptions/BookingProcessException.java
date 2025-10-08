package ru.otus.hw.exceptions;

public class BookingProcessException extends RuntimeException {
    public BookingProcessException(String message) {
        super(message);
    }
}
