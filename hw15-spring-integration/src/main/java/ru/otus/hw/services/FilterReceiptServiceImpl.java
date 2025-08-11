package ru.otus.hw.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.PaymentReceipt;

@Slf4j
@Service
public class FilterReceiptServiceImpl implements FilterReceiptService {

    private static final Double THRESHOLD_SUM = 500.0;

    @Override
    public boolean filter(PaymentReceipt paymentReceipt) {
        double sumReceipt = paymentReceipt
                .order()
                .values()
                .stream()
                .mapToDouble(Double::doubleValue)
                .sum();
        if (sumReceipt > THRESHOLD_SUM) {
            return true;
        } else {
            log.info(
                    "RECEIPT WAS FILTERED BY THRESHOLD SUM FOR CLIENT_ID: {}, SUM: {}",
                    paymentReceipt.clientId(), Math.round(sumReceipt));
            return false;
        }
    }
}
