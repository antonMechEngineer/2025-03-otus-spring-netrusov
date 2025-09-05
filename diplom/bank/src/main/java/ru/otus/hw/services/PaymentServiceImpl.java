package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.kafka.PaymentProducer;
import ru.otus.hw.models.Account;
import ru.otus.hw.models.Payment;
import ru.otus.hw.repositories.AccountRepository;
import ru.otus.hw.repositories.PaymentRepository;
import ru.otus.hw.repositories.UserRepository;

import java.math.BigDecimal;
import java.util.List;

import static ru.otus.hw.models.Payment.Status.CANCEL;
import static ru.otus.hw.models.Payment.Status.PAID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    private final UserRepository userRepository;

    private final AccountRepository accountRepository;

    private final PaymentProducer paymentProducer;

    @Override
    public List<Payment> findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow().getPayments();
    }

    public void create(Payment payment){
        paymentRepository.save(payment);
    }

    @Transactional
    @Override
    public Payment pay(String username, long id) {
        Payment payment = paymentRepository.findById(id).orElseThrow();
        Account account = userRepository.findByUsername(username).orElseThrow().getAccount();
        BigDecimal updatedBalance = account.getBalance().subtract(payment.getPrice());
        account.setBalance(updatedBalance);
        accountRepository.save(account);
        payment.setStatus(PAID);
        Payment confirmedPayment = paymentRepository.save(payment);
        paymentProducer.send(confirmedPayment);
        return confirmedPayment;
    }

    @Transactional
    @Override
    public Payment cancel(String username, long id) {
        Payment payment = paymentRepository.findById(id).orElseThrow();
        payment.setStatus(CANCEL);
        Payment canceledPayment = paymentRepository.save(payment);
        paymentProducer.send(canceledPayment);
        return canceledPayment;
    }
}


