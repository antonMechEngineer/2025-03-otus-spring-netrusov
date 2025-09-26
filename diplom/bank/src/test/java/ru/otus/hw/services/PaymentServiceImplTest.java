package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.kafka.PaymentProducer;
import ru.otus.hw.models.Account;
import ru.otus.hw.models.Payment;
import ru.otus.hw.models.User;
import ru.otus.hw.repositories.AccountRepository;
import ru.otus.hw.repositories.PaymentRepository;
import ru.otus.hw.repositories.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ru.otus.hw.models.Payment.Status.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Сервис платежей")
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PaymentProducer paymentProducer;

    @Mock
    private AclServiceWrapperService aclServiceWrapperService;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private User user;
    private Account account;
    private Payment payment;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal("1000.00"));
        user = new User(1L, "testUser", "password", "ROLE_USER", List.of(), account);
        account.setUser(user);
        payment = new Payment(1L, "book", 100L, user,
                new BigDecimal("200.00"), NOT_PAID, LocalDateTime.now());
    }

    @DisplayName("Положительный сценарий. Создание платежа")
    @Test
    void createPositive() {
        when(paymentRepository.save(any())).thenReturn(payment);
        paymentService.create(payment);
        verify(paymentRepository, times(1)).save(payment);
        verify(aclServiceWrapperService, times(1)).createPaymentPermission(payment);
    }

    @DisplayName("Положительный сценарий. Поиск платежей по username")
    @Test
    void findByUsernamePositive() {
        user.setPayments(List.of(payment));
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        List<Payment> result = paymentService.findByUsernameInternal("testUser");
        assertEquals(1, result.size());
        assertEquals(payment, result.get(0));
    }

    @DisplayName("Отрицательный сценарий. Поиск платежей по username, пользователь не найден")
    @Test
    void findByUsernameNegative() {
        when(userRepository.findByUsername("badUser")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> paymentService.findByUsernameInternal("badUser"));
    }

    @DisplayName("Положительный сценарий. Поиск платежа по id")
    @Test
    void findByIdPositive() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        Payment result = paymentService.findById(1L);
        assertEquals(payment, result);
    }

    @DisplayName("Отрицательный сценарий. Поиск платежа по id, платеж не найден")
    @Test
    void findByIdNegative() {
        when(paymentRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> paymentService.findById(99L));
    }

    @DisplayName("Положительный сценарий. Оплата платежа")
    @Test
    void payPositive() {
        when(paymentRepository.save(any())).thenReturn(payment);
        Payment result = paymentService.pay(payment);
        assertEquals(PAID, result.getStatus());
        assertEquals(new BigDecimal("800.00"), account.getBalance());
        verify(accountRepository, times(1)).save(account);
        verify(paymentRepository, times(1)).save(payment);
        verify(paymentProducer, times(1)).send(payment);
    }

    @DisplayName("Положительный сценарий. Отмена платежа")
    @Test
    void cancelPositive() {
        when(paymentRepository.save(any())).thenReturn(payment);
        Payment result = paymentService.cancel(payment);
        assertEquals(CANCEL, result.getStatus());
        verify(paymentRepository, times(1)).save(payment);
        verify(paymentProducer, times(1)).send(payment);
    }
}