package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Order;
import ru.otus.hw.models.Room;
import ru.otus.hw.models.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.*;
import static ru.otus.hw.models.Order.Status.PAID;

@SuppressWarnings("unused")
@DisplayName("Репозиторий заказов")
@DataJpaTest
@EnableJpaRepositories(basePackageClasses = OrderRepository.class)
@Transactional
public class JpaOrderRepositoryTest {

    @Autowired
    private OrderRepository jpaOrderRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private static final Integer EXPECTED_ORDER_SIZE = 1;

    private static final Order.Status SPECIFIC_STATUS = PAID;

    private Room room;

    private User user;

    private Order expectedOrder;

    private Order additionalOrder;

    @BeforeEach
    void beforeEach(){
        room = new Room(null, 999, Room.Type.LUX, new BigDecimal("1000.0"), List.of());
        user = new User(null, "testUsername", "testPassword", "ROLE_USER", List.of());
        expectedOrder = new Order(now(), now().plusDays(1), user, room);
        expectedOrder.setStatus(SPECIFIC_STATUS);
        additionalOrder = new Order(now(), now().plusDays(1), user, room);
    }

    @DisplayName("Положительный сценарий. Найти заказы по определенному статусу и созданных раньше определенного времени")
    @Test
    void findAuthorByStatusAndLessDateTimePositive() {
        LocalDateTime actualTimestamp = LocalDateTime.now().minusMinutes(2);
        LocalDateTime rangeForSeekingTimestamp = LocalDateTime.now().minusMinutes(1);
        expectedOrder.setCreatedAt(actualTimestamp);
        testEntityManager.persist(room);
        testEntityManager.persist(user);
        testEntityManager.persist(expectedOrder);
        testEntityManager.persist(additionalOrder);
        List<Order> actualOrders = jpaOrderRepository.findByStatusAndCreatedAtLessThanEqual(SPECIFIC_STATUS, rangeForSeekingTimestamp);
        assertFalse(actualOrders.isEmpty());
        assertEquals(EXPECTED_ORDER_SIZE, actualOrders.size());
        assertEquals(room.getRoomNumber(), actualOrders.getFirst().getRoom().getRoomNumber());
        assertEquals(user.getUsername(), actualOrders.getFirst().getUser().getUsername());
    }

    @DisplayName("Положительный сценарий. Найти заказы по определенному статусу и созданных позже определенного времени")
    @Test
    void findAuthorByStatusAndGreaterDateTimePositive() {
        LocalDateTime actualTimestamp = LocalDateTime.now().minusMinutes(1);
        LocalDateTime rangeForSeekingTimestamp = LocalDateTime.now().minusMinutes(2);
        LocalDateTime additionalOrderTimestamp = LocalDateTime.now().minusMinutes(3);
        expectedOrder.setCreatedAt(actualTimestamp);
        additionalOrder.setCreatedAt(additionalOrderTimestamp);
        testEntityManager.persist(room);
        testEntityManager.persist(user);
        testEntityManager.persist(expectedOrder);
        testEntityManager.persist(additionalOrder);
        List<Order> actualOrders = jpaOrderRepository.findByStatusAndCreatedAtGreaterThanEqual(SPECIFIC_STATUS, rangeForSeekingTimestamp);
        assertFalse(actualOrders.isEmpty());
        assertEquals(EXPECTED_ORDER_SIZE, actualOrders.size());
        assertEquals(room.getRoomNumber(), actualOrders.getFirst().getRoom().getRoomNumber());
        assertEquals(user.getUsername(), actualOrders.getFirst().getUser().getUsername());
    }

    @DisplayName("Положительный сценарий. Проверка, что комната свободна.")
    @Test
    void roomIsEmpty() {
        Room savedRoom = testEntityManager.persistAndFlush(room);
        testEntityManager.persist(user);
        testEntityManager.persist(expectedOrder);
        testEntityManager.persist(additionalOrder);
        assertFalse(jpaOrderRepository.existsByRoomIdAndStatusInAndBeginRentLessThanEqualAndEndRentGreaterThanEqual(
                savedRoom.getId(),
                List.of(SPECIFIC_STATUS),
                now().minusDays(3),
                now().minusDays(2)
        ));
    }

    @DisplayName("Отрицательный сценарий. Проверка, что комната занята.")
    @Test
    void roomIsBooked() {
        Room savedRoom = testEntityManager.persist(room);
        testEntityManager.persist(user);
        testEntityManager.persist(expectedOrder);
        testEntityManager.persist(additionalOrder);
        assertTrue(jpaOrderRepository.existsByRoomIdAndStatusInAndBeginRentLessThanEqualAndEndRentGreaterThanEqual(
                savedRoom.getId(),
                List.of(SPECIFIC_STATUS),
                now(),
                now().plusDays(1)
        ));
    }
}
