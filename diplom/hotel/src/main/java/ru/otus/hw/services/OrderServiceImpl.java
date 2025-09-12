package ru.otus.hw.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.kafka.PaymentProducer;
import ru.otus.hw.models.Order;
import ru.otus.hw.repositories.OrderRepository;
import ru.otus.hw.repositories.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static ru.otus.hw.models.Order.Status.AUTO_CANCEL;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

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
        Order createdOrder = orderRepository.save(order);
        aclServiceWrapperService.createPermission(createdOrder);
        return createdOrder;
    }

    @PreAuthorize("hasPermission(#order, 'WRITE')")
    @Transactional
    @Override
    public void updateStatus(Order order, Order.Status status) {
        updateStatusInternal(order, status);
    }

    @Override
    public void updateStatusInternal(Order order, Order.Status status) {
        order.setStatus(status);
        orderRepository.save(order);
        if (status == Order.Status.PAYMENT_REQUEST) {
            paymentProducer.send(order);
        }
    }

    @Override
    public List<LocalDate> findOccupiedDates(Long roomId) {
        return orderRepository.findAll().stream()
                .filter(o -> o.getRoom().getId().equals(roomId) && o.getStatus() == Order.Status.PAID)
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
                Order.Status.NOT_PAID,
                LocalDateTime.now().minusMinutes(2)
        );
        ordersToCancel.forEach(order -> order.setStatus(AUTO_CANCEL));
        orderRepository.saveAll(ordersToCancel);
    }
}


