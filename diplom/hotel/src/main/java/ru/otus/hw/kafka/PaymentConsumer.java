package ru.otus.hw.kafka;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import ru.otus.hw.kafka.dto.PaymentResp;
import ru.otus.hw.models.Order;
import ru.otus.hw.services.OrderService;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentConsumer {

    private final OrderService orderService;

    @KafkaListener(topics = "payment-response", containerFactory = "listenerContainerFactory")
    public void listen(@Payload PaymentResp paymentResp, Acknowledgment acknowledgment) {
        log.info("Received paymentResp!");
        if (paymentResp.isConfirmed()) {
            orderService.updateStatus(paymentResp.getBuyId(), Order.Status.PAID);
        } else {
            orderService.updateStatus(paymentResp.getBuyId(), Order.Status.CANCEL);
        }
        acknowledgment.acknowledge();
    }
}
