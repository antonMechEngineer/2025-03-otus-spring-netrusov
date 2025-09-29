package ru.otus.hw.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.otus.hw.kafka.dto.PaymentResp;
import ru.otus.hw.mappers.PaymentMapper;
import ru.otus.hw.models.Payment;

@Component
@RequiredArgsConstructor
public class PaymentProducer {

    private final static String TOPIC_RESP = "payment-response";

    private final KafkaTemplate<String, PaymentResp> kafkaTemplate;

    private final PaymentMapper paymentMapper;

    public void send(Payment payment) {
        kafkaTemplate.send(TOPIC_RESP, paymentMapper.toPaymentResp(payment));
    }
}
