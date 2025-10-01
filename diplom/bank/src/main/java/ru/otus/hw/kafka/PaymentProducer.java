package ru.otus.hw.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.otus.hw.kafka.dto.PaymentResp;
import ru.otus.hw.mappers.PaymentMapper;
import ru.otus.hw.models.Payment;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentProducer {

    private static final String ERROR_SENDING = "Sending error {}, message: {}";

    private static final String TOPIC_RESP = "payment-response";

    private final KafkaTemplate<String, PaymentResp> kafkaTemplate;

    private final PaymentMapper paymentMapper;

    public void send(Payment payment) {
        kafkaTemplate.send(TOPIC_RESP, paymentMapper.toPaymentResp(payment))
                .exceptionally(t -> {
                    log.error(ERROR_SENDING, t, payment);
                    return null;
                });
    }
}
