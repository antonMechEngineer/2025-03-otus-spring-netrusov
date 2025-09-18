package ru.otus.hw.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.BookingProcessException;
import ru.otus.hw.exceptions.PaymentProcessException;
import ru.otus.hw.kafka.PaymentProducer;
import ru.otus.hw.models.Order;
import ru.otus.hw.models.Order.Status;
import ru.otus.hw.repositories.OrderRepository;
import ru.otus.hw.repositories.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.String.format;
import static ru.otus.hw.models.Order.Status.AUTO_CANCEL;
import static ru.otus.hw.models.Order.Status.PAYMENT_REQUEST;
import static ru.otus.hw.models.Order.Status.PAID;
import static ru.otus.hw.models.Order.Status.NOT_PAID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private static final String ERROR_CONFIRM_PAYMENT = "Error confirm payment orderId = %s";

    private static final String ERROR_CREATE_BOOKING = "Error create book for room id = %s. Room is already occupied!";

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    private final PaymentProducer paymentProducer;

    private final AclServiceWrapperService aclServiceWrapperService;

    @PostFilter("hasPermission(filterObject, 'READ')")
    @Override
    public List<Order> findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow().getOrders();
    }

    @PreAuthorize("hasPermission(#id, 'ru.otus.hw.models.Order', 'READ')")
    @Override
    public Order findById(Long id) {
        return findByIdInternal(id);
    }

    @Override
    public Order findByIdInternal(Long id) {
        return orderRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    @Override
    public Order create(Order order) {
        if (orderRepository.existsByRoomIdAndStatusInAndBeginRentLessThanEqualAndEndRentGreaterThanEqual(
                order.getRoom().getId(),
                List.of(NOT_PAID, PAID, PAYMENT_REQUEST),
                order.getBeginRent(),
                order.getEndRent())
        ) {
            throw new BookingProcessException(format(ERROR_CREATE_BOOKING, order.getRoom().getId()));
        }
        Order createdOrder = orderRepository.save(order);
        aclServiceWrapperService.createPermission(createdOrder);
        return createdOrder;
    }

    @PreAuthorize("hasPermission(#order, 'WRITE')")
    @Override
    public void updateStatus(Order order, Status status) {
        updateStatusInternal(order, status);
    }

    @Override
    @Transactional
    public void updateStatusInternal(Order order, Status status) {
        if (status == PAID && order.getStatus() != PAYMENT_REQUEST) {
            throw new PaymentProcessException(format(ERROR_CONFIRM_PAYMENT, order.getId()));
        }
        order.setStatus(status);
        orderRepository.save(order);
        if (status == PAYMENT_REQUEST) {
            paymentProducer.send(order);
        }
    }

    @Override
    public List<LocalDate> findOccupiedDates(Long roomId) {
        return orderRepository.findAll().stream()
                .filter(o -> o.getRoom().getId().equals(roomId) && o.getStatus() == PAID ||
                        o.getRoom().getId().equals(roomId) && o.getStatus() == PAYMENT_REQUEST)
                .flatMap(order -> generateDateRangeStream(order.getBeginRent(), order.getEndRent()))
                .toList();
    }

    private Stream<LocalDate> generateDateRangeStream(LocalDate start, LocalDate end) {
        return Stream.iterate(
                start,
                date -> !date.isAfter(end.minusDays(1)),
                date -> date.plusDays(1));
    }

    @Override
    @Scheduled(initialDelay = 120_000, fixedRate = 600_000)
    public void checkAndCancelOrders() {
        List<Order> ordersToCancel = orderRepository.findByStatusAndCreatedAtLessThanEqual(
                NOT_PAID,
                LocalDateTime.now().minusMinutes(2)
        );
        ordersToCancel.forEach(order -> order.setStatus(AUTO_CANCEL));
        orderRepository.saveAll(ordersToCancel);
    }
}


