package ru.otus.hw;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.otus.hw.domain.OrganizationType;
import ru.otus.hw.domain.PaymentReceipt;
import ru.otus.hw.services.AdvertisementProviderGateway;
import ru.otus.hw.services.AdvertisementService;
import ru.otus.hw.services.FilterReceiptService;
import ru.otus.hw.services.PaymentReceiptServiceImpl;

import java.util.Map;

@SpringBootTest
public class AdvertisementProviderGatewayTest {

    @Autowired
    private AdvertisementProviderGateway advertisementProviderGateway;

    @Autowired
    private AdvertisementService advertisementService;

    @Autowired
    private FilterReceiptService filterReceiptService;

    @MockitoBean
    private PaymentReceiptServiceImpl paymentReceiptService;

    @Test
    void testForSmsFlow() {
        PaymentReceipt paymentReceipt = new PaymentReceipt(1L, Map.of("abc", 999999.0), OrganizationType.MEDICAL);
        String result = advertisementProviderGateway.process(paymentReceipt);
        Assertions.assertEquals("SMS: Some MEDICAL advertisement!", result);
    }

    @Test
    void testForPushFlow() {
        PaymentReceipt paymentReceipt = new PaymentReceipt(1L, Map.of("abc", 999999.0), OrganizationType.GROCERY);
        String result = advertisementProviderGateway.process(paymentReceipt);
        Assertions.assertEquals("PUSH: Some GROCERY advertisement!", result);
    }

    @Test
    void testForFilterByOrderSum() {
        PaymentReceipt paymentReceipt = new PaymentReceipt(1L, Map.of("abc", 0.0), OrganizationType.GROCERY);
        String result = advertisementProviderGateway.process(paymentReceipt);
        Assertions.assertNull(result);
    }
}
