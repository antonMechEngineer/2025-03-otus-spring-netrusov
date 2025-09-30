package ru.otus.hw.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.otus.hw.kafka.dto.PaymentReq;
import ru.otus.hw.models.Order;

@Component
@RequiredArgsConstructor
public class PaymentProducer {

    private static final String TOPIC_REQ = "payment-request";

    private static final String BUY = "Hotel";

    private final KafkaTemplate<String, PaymentReq> kafkaTemplate;

    public void send(Order o, PaymentReq.ActionType actionType) {
        kafkaTemplate.send(
                TOPIC_REQ,
                new PaymentReq(BUY, o.getId(), o.getTotalPrice().doubleValue(), o.getUser().getUsername(), actionType));
    }
}
