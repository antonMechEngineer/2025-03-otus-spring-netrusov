package ru.otus.hw.kafka;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.otus.hw.kafka.dto.PaymentReq;
import ru.otus.hw.mapper.PaymentMapper;
import ru.otus.hw.services.PaymentService;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentConsumer {

    private final PaymentService paymentService;

    private final PaymentMapper paymentMapper;

    @KafkaListener(topics = "payment-request", containerFactory = "listenerContainerFactory")
    public void listen(@Payload PaymentReq paymentReq) {
        log.info("Прочитал запись paymentReq!");
        paymentService.create(paymentMapper.fromPaymentReq(paymentReq));
    }
}
