package ru.otus.hw.indicators;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Order;
import ru.otus.hw.provider.RentabilityProvider;
import ru.otus.hw.services.OrderService;

import java.math.BigDecimal;

@SuppressWarnings("unused")
@Slf4j
@Component
@RequiredArgsConstructor
public class RentabilityIndicator implements HealthIndicator {

    private static final String UP_MSG = "Rentability is ok.";

    private static final String DOWN_MSG = "Rentability is bad!";

    private static final String ERROR_MSG = "Balance indicator is unavailable!";

    private final RentabilityProvider rentabilityProvider;

    private final OrderService orderService;

    @Override
    public Health health() {
        try {
            if (orderService.findPaidLastDays(rentabilityProvider.period())
                    .stream()
                    .map(Order::getTotalPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .compareTo(BigDecimal.valueOf(rentabilityProvider.threshold())) < 0) {
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
