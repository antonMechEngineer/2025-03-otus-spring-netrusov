package ru.otus.hw.services;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.hw.domain.PaymentReceipt;

@MessagingGateway(defaultReplyTimeout = "2000")
public interface AdvertisementProviderGateway {

    @Gateway(requestChannel = "paymentReceiptChannel", replyChannel = "advertisementChannel")
    String process(PaymentReceipt paymentReceipt);
}
