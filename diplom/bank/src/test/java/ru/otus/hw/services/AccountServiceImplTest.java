package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Account;
import ru.otus.hw.models.User;
import ru.otus.hw.repositories.AccountRepository;
import ru.otus.hw.repositories.UserRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Сервис счетов")
class AccountServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private User user;
    private Account account;
    @BeforeEach
    void setUp() {
        account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal("1000.00"));
        user = new User(1L, "testUser", "password", "ROLE_USER", List.of(), account);
        account.setUser(user);
    }

    @DisplayName("Положительный сценарий. Поиск счёта по username")
    @Test
    void findByUsernamePositive() {
        user.setAccount(account);
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        Account result = accountService.findByUsername("testUser");
        assertEquals(account, result);
    }

    @DisplayName("Отрицательный сценарий. Поиск счета по username, пользователь не найден")
    @Test
    void findByUsernameNegative() {
        when(userRepository.findByUsername("badUser")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> accountService.findByUsername("badUser"));
    }

    @DisplayName("Положительный сценарий. Поиск всех счетов")
    @Test
    void findByIdPositive() {
        when(accountRepository.findAll()).thenReturn(List.of(account));
        List<Account> result = accountService.findAll();
        assertEquals(account, result.getFirst());
    }
}