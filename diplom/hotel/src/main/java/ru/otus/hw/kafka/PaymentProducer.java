package ru.otus.hw.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.otus.hw.kafka.dto.PaymentReq;
import ru.otus.hw.models.Order;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentProducer {

    private static final String ERROR_SENDING = "Sending error {}, message: {}";

    private static final String TOPIC_REQ = "payment-request";

    private static final String BUY = "Hotel";

    private final KafkaTemplate<String, PaymentReq> kafkaTemplate;

    public void send(Order o, PaymentReq.ActionType actionType) {
        var rq = new PaymentReq(BUY, o.getId(), o.getTotalPrice().doubleValue(), o.getUser().getUsername(), actionType);
        kafkaTemplate.send(TOPIC_REQ, rq)
                .exceptionally(t -> {
                    log.error(ERROR_SENDING, t, rq);
                    return null;
                });
    }
}
