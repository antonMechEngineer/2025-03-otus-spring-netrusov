package ru.otus.hw.rest.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.otus.hw.models.Order;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class OrderDto {

    private Long id;

    @NotNull(message = "Begin rent date cannot be null")
    @FutureOrPresent(message = "Begin rent date must be in the future or present")
    private LocalDate beginRent;

    @NotNull(message = "End rent date cannot be null")
    @Future(message = "End rent date must be in the future")
    private LocalDate endRent;

    @Size(max = 50, message = "Username cannot exceed 50 characters")
    private String username;

    @NotNull(message = "Room number cannot be null")
    @Positive(message = "Room number must be positive")
    private Integer roomNumber;


    @Positive(message = "Total price must be positive")
    @Digits(integer = 17, fraction = 2, message = "Total price must have maximum 17 integer digits and 2 fractional digits")
    private BigDecimal totalPrice;

    private Order.Status status;
}
