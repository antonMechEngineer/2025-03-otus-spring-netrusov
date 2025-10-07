package ru.otus.hw.provider;

public interface OrderTtlProvider {

     Long getNotPaidOrder();

     Long getRequestedPaymentOrder();
}
