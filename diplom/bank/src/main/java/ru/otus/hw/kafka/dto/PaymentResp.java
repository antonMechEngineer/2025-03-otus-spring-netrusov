package ru.otus.hw.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentResp {

   private long buyId;

   private boolean isConfirmed;

}
