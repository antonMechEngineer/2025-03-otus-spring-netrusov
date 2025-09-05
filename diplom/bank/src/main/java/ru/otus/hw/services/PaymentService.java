package ru.otus.hw.services;

import ru.otus.hw.models.Payment;

import java.util.List;

public interface PaymentService {

    List<Payment> findByUsername(String username);

    void create(Payment payment);

    Payment pay(String username, long id);

    Payment cancel(String username, long id);

}
