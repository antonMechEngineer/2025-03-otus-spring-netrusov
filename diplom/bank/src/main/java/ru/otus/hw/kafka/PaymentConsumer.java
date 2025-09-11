package ru.otus.hw.kafka;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.otus.hw.kafka.dto.PaymentReq;
import ru.otus.hw.mapper.PaymentMapper;
import ru.otus.hw.models.Payment;
import ru.otus.hw.models.User;
import ru.otus.hw.services.PaymentService;
import ru.otus.hw.services.UserService;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentConsumer {

    private final PaymentService paymentService;

    private final UserService userService;

    @KafkaListener(topics = "payment-request", containerFactory = "listenerContainerFactory")
    public void listen(@Payload PaymentReq paymentReq, Acknowledgment acknowledgment) {
        log.info("Received paymentReq!");
        User currentUser = userService.findByUsername(paymentReq.getUsername());
        paymentService.create(new Payment(null,
                paymentReq.getBuy(),
                paymentReq.getBuyId(),
                currentUser,
                paymentReq.getPrice(),
                Payment.Status.NOT_PAID,
                LocalDateTime.now()));
        acknowledgment.acknowledge();
    }
}
