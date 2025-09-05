package ru.otus.hw.indicators;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import ru.otus.hw.services.RoomService;

@Component
@RequiredArgsConstructor
public class CurrentBookedIndicator implements HealthIndicator {

    private final RoomService roomService;

    @Override
    public Health health() {
        if (roomService.findAll().size() == 0) {
            return Health.down()
                    .status(Status.DOWN)
                    .withDetail("message", "All rooms are empty, need checking!")
                    .build();
        } else {
            return Health.up().withDetail("message", "There are visitors in the room.").build();
        }
    }


}
