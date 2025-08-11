package ru.otus.hw.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.OrganizationType;
import ru.otus.hw.domain.PaymentReceipt;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

@Service
@Slf4j
public class PaymentReceiptServiceImpl implements PaymentReceiptService {

    private static final OrganizationType[] ORGANIZATION_TYPES = OrganizationType.values();

    private final AdvertisementProviderGateway adsProvider;

    public PaymentReceiptServiceImpl(AdvertisementProviderGateway adsProvider) {
        this.adsProvider = adsProvider;
    }

    @Override
    public void executeFlowAdsFromReceipts() {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        for (int i = 0; i < 10; i++) {
            pool.execute(() -> {
                PaymentReceipt paymentReceipt = generatePaymentReceipt();
                log.info("Payment receipt {} generated", paymentReceipt);
                String advertisement = adsProvider.process(paymentReceipt);
                log.info("Final result from ads provider: {}", advertisement);
            });
            delay();
        }
    }

    private static PaymentReceipt generatePaymentReceipt() {
        OrganizationType organizationType = ORGANIZATION_TYPES[new Random().nextInt(ORGANIZATION_TYPES.length)];
        return new PaymentReceipt(
                Math.abs(new Random().nextLong()),
                generateOrder(organizationType),
                organizationType);
    }

    private static Map<String, Double> generateOrder(OrganizationType organizationType) {
        Map<String, Double> order = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            String goodTitle =
                    "Any " + organizationType.name() + " good " + System.currentTimeMillis() / 1_000_000;
            order.put(goodTitle, new Random().nextDouble() * 1000);
        }
        return order;
    }

    private void delay() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
