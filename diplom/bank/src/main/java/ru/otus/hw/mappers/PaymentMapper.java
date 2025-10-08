package ru.otus.hw.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.hw.kafka.dto.PaymentResp;
import ru.otus.hw.models.Payment;
import ru.otus.hw.rest.dto.PaymentDto;


@Mapper(componentModel = "spring")
public interface PaymentMapper {


    @Mapping(source = "user.username", target = "username")
    PaymentDto toDto(Payment payment);

    Payment fromDto(PaymentDto paymentDto);

    @Mapping(target = "confirmed", expression = "java(payment.getStatus() == Payment.Status.PAID)")
    PaymentResp toPaymentResp(Payment payment);


}
