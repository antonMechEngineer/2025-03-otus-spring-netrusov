package ru.otus.hw.indicators;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.Health;
import ru.otus.hw.models.Account;
import ru.otus.hw.models.User;
import ru.otus.hw.services.AccountService;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.boot.actuate.health.Status.DOWN;
import static org.springframework.boot.actuate.health.Status.UP;

@ExtendWith(MockitoExtension.class)
@DisplayName("Индикатор баланса банка")
public class BalanceIndicatorTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private BalanceIndicator balanceIndicator;

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

    @DisplayName("Положительный сценарий. Сообщение положительного состояния баланса")
    @Test
    void healthPositive(){
        Account accountPositive = new Account(1L, new BigDecimal(2000), user);
        when(accountService.findAll()).thenReturn(List.of(account, accountPositive));
        Health actualHealth = balanceIndicator.health();
        Assertions.assertEquals(UP, actualHealth.getStatus());

    }

    @DisplayName("Отрицательный сценарий. Проверка индикации перехода баланса в отрицательные значения")
    @Test
    void healthNegative(){
        Account accountNegative = new Account(1L, new BigDecimal(-2000), user);
        when(accountService.findAll()).thenReturn(List.of(account, accountNegative));
        Health actualHealth = balanceIndicator.health();
        Assertions.assertEquals(DOWN, actualHealth.getStatus());
    }
}
