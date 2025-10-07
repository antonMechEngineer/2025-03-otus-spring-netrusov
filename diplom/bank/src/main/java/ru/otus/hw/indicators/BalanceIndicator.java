package ru.otus.hw.indicators;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Account;
import ru.otus.hw.services.AccountService;

import java.math.BigDecimal;

@SuppressWarnings("unused")
@Slf4j
@Component
@RequiredArgsConstructor
public class BalanceIndicator implements HealthIndicator {

    private static final String UP_MSG = "Balances are ok!";

    private static final String DOWN_MSG = "Sum balances are negative, need checking!";

    private static final String ERROR_MSG = "Balance indicator is unavailable!";

    private final AccountService accountService;

    @Override
    public Health health() {
        try {
            if (accountService.findAll()
                    .stream()
                    .map(Account::getBalance)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .compareTo(BigDecimal.ZERO) < 0) {
                return Health.down()
                        .status(Status.DOWN)
                        .withDetail("message", DOWN_MSG)
                        .build();
            }
            return Health.up().withDetail("message", UP_MSG).build();
        } catch (Exception exception) {
            log.error(ERROR_MSG, exception);
            return Health.unknown()
                    .withDetail("message", ERROR_MSG)
                    .build();
        }
    }
}
