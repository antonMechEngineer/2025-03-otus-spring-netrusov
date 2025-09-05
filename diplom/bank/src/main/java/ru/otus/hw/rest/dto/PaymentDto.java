package ru.otus.hw.rest.dto;

import lombok.Data;
import ru.otus.hw.models.Payment;

import java.math.BigDecimal;

@Data
public class PaymentDto {

    private long id;

    private String buy;

    private long buyId;

    private String username;

    private BigDecimal price;

    private Payment.Status status;

}
