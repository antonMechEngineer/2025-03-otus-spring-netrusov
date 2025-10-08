package ru.otus.hw.services;

import ru.otus.hw.models.Payment;

public interface AclServiceWrapperService {

    void createPaymentPermission(Payment payment);
}
