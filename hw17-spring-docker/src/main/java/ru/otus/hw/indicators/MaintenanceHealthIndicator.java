package ru.otus.hw.indicators;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

@Component
public class MaintenanceHealthIndicator implements HealthIndicator {

    private static final long MAINTENANCE_INTERVAL_SEC = 10;

    @Override
    public Health health() {
        if (isRunning()) {
            return Health.down()
                    .status(Status.DOWN)
                    .withDetail("message", "Service state is technological maintenance!")
                    .build();
        } else {
            return Health.up().withDetail("message", "Service state is running!").build();
        }
    }

    private boolean isRunning() {
        return (System.currentTimeMillis() / (MAINTENANCE_INTERVAL_SEC * 1000)) % 2 == 0;
    }

}
