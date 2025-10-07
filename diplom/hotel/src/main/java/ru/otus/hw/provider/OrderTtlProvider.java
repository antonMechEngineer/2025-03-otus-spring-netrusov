package ru.otus.hw.provider;

public interface OrderTtlProvider {

     Long notPaidOrder();

     Long requestedPaymentOrder();
}
