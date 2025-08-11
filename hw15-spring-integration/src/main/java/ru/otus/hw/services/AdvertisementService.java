package ru.otus.hw.services;

import ru.otus.hw.domain.Advertisement;
import ru.otus.hw.domain.PaymentReceipt;

public interface AdvertisementService {
	Advertisement calculate(PaymentReceipt paymentReceipt);
}
