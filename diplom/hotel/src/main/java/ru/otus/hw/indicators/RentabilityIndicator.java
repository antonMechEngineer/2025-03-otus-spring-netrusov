package ru.otus.hw.indicators;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Order;
import ru.otus.hw.services.OrderService;

import java.math.BigDecimal;

@SuppressWarnings("unused")
@Component
@RequiredArgsConstructor
public class RentabilityIndicator implements HealthIndicator {

    private static final Integer RENTABILITY_PERIOD = 1;

    private final OrderService orderService;

    @Value("${rentability-threshold}")
    private Long rentabilityThreshold;

    @Override
    public Health health() {
        if (orderService
                .findPaidLastDays(RENTABILITY_PERIOD)
                .stream()
                .map(Order::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .compareTo(BigDecimal.valueOf(rentabilityThreshold)) < 0) {
            return Health.down()
                    .status(Status.DOWN)
                    .withDetail("message", "Rentability is bad.")
                    .build();
        } else {
            return Health.up().withDetail("message", "Rentability is ok.").build();
        }
    }


}
