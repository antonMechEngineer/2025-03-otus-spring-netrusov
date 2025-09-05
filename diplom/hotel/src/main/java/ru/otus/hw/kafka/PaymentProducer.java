package ru.otus.hw.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.otus.hw.kafka.dto.PaymentReq;
import ru.otus.hw.mapper.PaymentMapper;
import ru.otus.hw.models.Order;

@Component
@RequiredArgsConstructor
public class PaymentProducer {

    private final static String TOPIC_REQ = "payment-request";

    private final static String BUY = "Hotel";

    private final KafkaTemplate<String, PaymentReq> kafkaTemplate;

    private final PaymentMapper paymentMapper;

    public void send(Order o) {
        kafkaTemplate.send(TOPIC_REQ, new PaymentReq(BUY, o.getId(), o.getTotalPrice(), o.getUser().getUsername()));
    }
}
