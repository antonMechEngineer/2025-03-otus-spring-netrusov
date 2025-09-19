package ru.otus.hw.indicators;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Account;
import ru.otus.hw.services.AccountService;

import java.math.BigDecimal;

@SuppressWarnings("unused")
@Component
@RequiredArgsConstructor
public class BalanceIndicator implements HealthIndicator {

    private final AccountService accountService;

    @Override
    public Health health() {
        if (accountService.findAll()
                .stream()
                .map(Account::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .compareTo(BigDecimal.ZERO) < 0) {
            return Health.down()
                    .status(Status.DOWN)
                    .withDetail("message", "Sum balances are negative, need checking!")
                    .build();
        } else {
            return Health.up().withDetail("message", "Balances are ok!").build();
        }
    }
}
