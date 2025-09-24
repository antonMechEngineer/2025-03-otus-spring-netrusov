package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.exceptions.BookingProcessException;
import ru.otus.hw.exceptions.PaymentProcessException;
import ru.otus.hw.kafka.PaymentProducer;
import ru.otus.hw.models.Order;
import ru.otus.hw.models.Room;
import ru.otus.hw.models.User;
import ru.otus.hw.repositories.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.otus.hw.kafka.dto.PaymentReq.ActionType.PAY;
import static ru.otus.hw.models.Order.Status.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Сервис заказов")
public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private AclServiceWrapperService aclServiceWrapperService;

    @Mock
    private PaymentProducer paymentProducer;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order notPaidOrder;

    private Order paidOrder;

    private Order paymentRequestOrder;

    private Room room;

    @BeforeEach
    void setUp() {
        room = new Room(1L, 999, Room.Type.LUX, new BigDecimal("1000.0"), List.of());
        User user = new User(null, "testUsername", "testPassword", "ROLE_USER", List.of());
        notPaidOrder = new Order(now(), now().plusDays(1), user, room);
        paidOrder = new Order(now().plusDays(2), now().plusDays(3), user, room);
        paidOrder.setStatus(PAID);
        paymentRequestOrder = new Order(now().plusDays(4), now().plusDays(5), user, room);
        paymentRequestOrder.setStatus(PAYMENT_REQUEST);
    }

    @DisplayName("Положительный сценарий. Проверка создания заказа.")
    @Test
    void createPositive() {
        when(orderRepository.existsByRoomIdAndStatusInAndBeginRentLessThanEqualAndEndRentGreaterThanEqual(
                any(), any(), any(), any())).thenReturn(false);
        orderService.create(notPaidOrder);
        verify(orderRepository, times(1))
                .existsByRoomIdAndStatusInAndBeginRentLessThanEqualAndEndRentGreaterThanEqual(any(), any(), any(), any());
        verify(orderRepository, times(1)).save(any());
        verify(aclServiceWrapperService, times(1)).createPermission(any());
    }

    @DisplayName("Отрицательный сценарий. Проверка запрета на создание заказа.")
    @Test
    void createNegative() {
        when(orderRepository.existsByRoomIdAndStatusInAndBeginRentLessThanEqualAndEndRentGreaterThanEqual(
                any(), any(), any(), any())).thenReturn(true);
        assertThrows(BookingProcessException.class, () -> orderService.create(notPaidOrder));
    }

    @DisplayName("Положительный сценарий. Обновление статуса заказа до PAYMENT_REQUEST.")
    @Test
    void updatePositivePaymentRequest() {
        orderService.updateStatusInternal(notPaidOrder, PAYMENT_REQUEST);
        assertEquals(PAYMENT_REQUEST, notPaidOrder.getStatus());
        verify(orderRepository, times(1)).save(notPaidOrder);
        verify(paymentProducer, times(1)).send(notPaidOrder, PAY);
    }


    @DisplayName("Положительный сценарий. Обновление статуса заказа до PAID.")
    @Test
    void updatePositivePaid() {
        notPaidOrder.setStatus(PAYMENT_REQUEST);
        orderService.updateStatusInternal(notPaidOrder, PAID);
        assertEquals(PAID, notPaidOrder.getStatus());
        verify(orderRepository, times(1)).save(notPaidOrder);
        verify(paymentProducer, times(0)).send(any(), any());
    }

    @DisplayName("Отрицательный сценарий. Обновление статуса до не консистентного состояния.")
    @Test
    void updateNegative() {
        assertThrows(PaymentProcessException.class, () -> orderService.updateStatusInternal(notPaidOrder, PAID));
    }

    @DisplayName("Положительный сценарий. Найдены занятые даты для комнаты")
    @Test
    void findOccupiedDatesPositive() {
        List<LocalDate> expectedDates = List.of(
                paidOrder.getBeginRent(), paidOrder.getEndRent(),
                paymentRequestOrder.getBeginRent(), paymentRequestOrder.getEndRent());
        when(orderRepository.findAll()).thenReturn(List.of(notPaidOrder, paidOrder, paymentRequestOrder));
        List<LocalDate> actualDates = orderService.findOccupiedDates(room.getId());
        assertEquals(expectedDates, actualDates);
    }

    @DisplayName("Положительный сценарий. Найти оплаченные заказы за последние n дней")
    @Test
    void findPaidLastDays() {
        orderService.findPaidLastDays(5);
        verify(orderRepository, times(1)).findByStatusAndCreatedAtGreaterThanEqual(any(), any());
    }

    @DisplayName("Положительный сценарий. Автоматическая отмена не оплаченных заказов")
    @Test
    void cancelNotPaidOrders() {
        when(orderRepository.findByStatusAndCreatedAtLessThanEqual(any(), any())).thenReturn(List.of(notPaidOrder));
        orderService.setNotPaidOrderTtl(5L);
        orderService.cancelNotPaidOrders();
        assertEquals(AUTO_CANCEL, notPaidOrder.getStatus());
        verify(orderRepository, times(1)).saveAll(any());
        verify(orderRepository, times(1)).findByStatusAndCreatedAtLessThanEqual(any(), any());
    }

    @DisplayName("Положительный сценарий. Автоматическая отмена заказов зависших в статусе PAYMENT_REQUEST")
    @Test
    void cancelPaymentRequestOrders() {
        when(orderRepository.findByStatusAndCreatedAtLessThanEqual(any(), any())).thenReturn(List.of(paymentRequestOrder));
        orderService.setRequestedPaymentOrderTtl(5L);
        orderService.cancelPayRequestedOrders();
        assertEquals(AUTO_CANCEL, paymentRequestOrder.getStatus());
        verify(orderRepository, times(1)).saveAll(any());
        verify(orderRepository, times(1)).findByStatusAndCreatedAtLessThanEqual(any(), any());
    }
}
