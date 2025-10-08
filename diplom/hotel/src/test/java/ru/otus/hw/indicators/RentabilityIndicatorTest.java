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
import ru.otus.hw.models.Order;
import ru.otus.hw.models.Room;
import ru.otus.hw.models.User;
import ru.otus.hw.provider.RentabilityProvider;
import ru.otus.hw.services.OrderService;

import java.math.BigDecimal;
import java.util.List;

import static java.time.LocalDate.now;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.boot.actuate.health.Status.DOWN;
import static org.springframework.boot.actuate.health.Status.UP;
import static ru.otus.hw.models.Order.Status.PAID;

@ExtendWith(MockitoExtension.class)
@DisplayName("Индикатор рентабельности отеля")
public class RentabilityIndicatorTest {

    @Mock
    private OrderService orderService;

    @Mock
    private RentabilityProvider rentabilityProvider;

    @InjectMocks
    private RentabilityIndicator rentabilityIndicator;

    private Order order1;

    private Order order2;

    @BeforeEach
    void setUp() {
        Room room = new Room(1L, 999, Room.Type.LUX, new BigDecimal("1000.0"), List.of());
        var user = new User(null, "testUsername", "testPassword", "ROLE_USER", List.of());
        order1 = new Order(now(), now().plusDays(1), user, room);
        order1.setStatus(PAID);
        order2 = new Order(now().plusDays(2), now().plusDays(3), user, room);
        order2.setStatus(PAID);
    }

    @DisplayName("Положительный сценарий. Отель рентабелен")
    @Test
    void healthPositive(){
        when(rentabilityProvider.threshold()).thenReturn(1999L);
        when(rentabilityProvider.period()).thenReturn(1);
        when(orderService.findPaidLastDays(any())).thenReturn(List.of(order2, order1));
        Health actualHealth = rentabilityIndicator.health();
        Assertions.assertEquals(UP, actualHealth.getStatus());
    }

    @DisplayName("Отрицательный сценарий. Отель не рентабелен")
    @Test
    void healthNegative(){
        when(rentabilityProvider.threshold()).thenReturn(1999L);
        when(rentabilityProvider.period()).thenReturn(1);
        when(orderService.findPaidLastDays(any())).thenReturn(List.of(order2));
        Health actualHealth = rentabilityIndicator.health();
        Assertions.assertEquals(DOWN, actualHealth.getStatus());
    }
}
