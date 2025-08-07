package ru.otus.hw.services;

import ru.otus.hw.domain.PaymentReceipt;

public interface FilterReceiptService {

    boolean filter(PaymentReceipt paymentReceipt);
}
