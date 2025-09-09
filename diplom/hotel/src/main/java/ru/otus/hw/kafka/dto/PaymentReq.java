package ru.otus.hw.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentReq {

    private String buy;

    private long buyId;

    private BigDecimal price;

    private String username;
}
