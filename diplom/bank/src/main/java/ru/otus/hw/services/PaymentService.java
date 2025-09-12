package ru.otus.hw.services;

import jakarta.persistence.EntityNotFoundException;
import ru.otus.hw.models.Payment;

import java.util.List;

public interface PaymentService {

    List<Payment> findByUsername(String username);

    List<Payment> findByUsernameInternal(String username);

    Payment findById(Long id);

    void create(Payment payment);

    Payment pay(Payment payment);

    Payment cancel(Payment payment);

}
