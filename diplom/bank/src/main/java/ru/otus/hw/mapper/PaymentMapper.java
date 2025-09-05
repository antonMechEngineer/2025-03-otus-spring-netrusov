package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.hw.kafka.dto.PaymentReq;
import ru.otus.hw.kafka.dto.PaymentResp;
import ru.otus.hw.models.Payment;
import ru.otus.hw.rest.dto.PaymentDto;


@Mapper(componentModel = "spring")
public interface PaymentMapper {


    @Mapping(source = "user.username", target = "username")
    PaymentDto toDto(Payment payment);

    Payment fromDto(PaymentDto paymentDto);

    Payment fromPaymentReq(PaymentReq paymentReq);

    @Mapping(target = "isConfirmed", expression = "java(payment.getStatus() == Payment.Status.PAID)")
    PaymentResp toPaymentResp(Payment payment);


}
