package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
