package ru.otus.hw.exceptions;

public class PaymentProcessException extends RuntimeException {
    public PaymentProcessException(String message) {
        super(message);
    }
}
