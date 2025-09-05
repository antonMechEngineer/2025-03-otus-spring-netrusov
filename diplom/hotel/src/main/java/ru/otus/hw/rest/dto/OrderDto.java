package ru.otus.hw.rest.dto;

import lombok.Data;
import ru.otus.hw.models.Order;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class OrderDto {

    private long id = 0L;

    private LocalDate beginRent;

    private LocalDate endRent;

    private String username;

    private Integer roomNumber;

    private BigDecimal totalPrice;

    private Order.Status status;
}
