package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.kafka.PaymentProducer;
import ru.otus.hw.models.Payment;
import ru.otus.hw.repositories.AccountRepository;
import ru.otus.hw.repositories.PaymentRepository;
import ru.otus.hw.repositories.UserRepository;

import java.util.List;

import static java.lang.String.format;
import static ru.otus.hw.models.Payment.Status.CANCEL;
import static ru.otus.hw.models.Payment.Status.PAID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private static final String ERROR_USER_NOT_FOUND = "User name = %s not found and payment respectively!";

    private static final String ERROR_PAYMENT_NOT_FOUND = "Payment with id = %d not found!";

    private final PaymentRepository paymentRepository;

    private final UserRepository userRepository;

    private final AccountRepository accountRepository;

    private final PaymentProducer paymentProducer;

    private final AclServiceWrapperService aclServiceWrapperService;

    @PostFilter("hasPermission(filterObject, 'READ')")
    @Override
    public List<Payment> findByUsername(String username) {
        return findByUsernameInternal(username);
    }

    @Override
    public List<Payment> findByUsernameInternal(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(format(ERROR_USER_NOT_FOUND, username)))
                .getPayments();
    }

    @PreAuthorize("hasPermission(#id, 'ru.otus.hw.models.Payment', 'READ')")
    @Override
    public Payment findById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(format(ERROR_PAYMENT_NOT_FOUND, id)));
    }

    @Transactional
    @Override
    public void create(Payment payment) {
        var createdPayment = paymentRepository.save(payment);
        aclServiceWrapperService.createPaymentPermission(createdPayment);
    }

    @PreAuthorize("hasPermission(#payment, 'WRITE')")
    @Transactional
    @Override
    public Payment pay(Payment payment) {
        var account = payment.getUser().getAccount();
        var updatedBalance = account.getBalance().subtract(payment.getPrice());
        account.setBalance(updatedBalance);
        accountRepository.save(account);
        payment.setStatus(PAID);
        var confirmedPayment = paymentRepository.save(payment);
        paymentProducer.send(confirmedPayment);
        return confirmedPayment;
    }

    @PreAuthorize("hasPermission(#payment, 'WRITE')")
    @Transactional
    @Override
    public Payment cancel(Payment payment) {
        payment.setStatus(CANCEL);
        Payment canceledPayment = paymentRepository.save(payment);
        paymentProducer.send(canceledPayment);
        return canceledPayment;
    }
}