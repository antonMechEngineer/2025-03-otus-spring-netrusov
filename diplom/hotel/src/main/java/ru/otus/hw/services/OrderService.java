package ru.otus.hw.services;

import org.springframework.scheduling.annotation.Scheduled;
import ru.otus.hw.models.Order;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {

    List<Order> findByUsername(String username);

    Order findById(Long id);

    Order findByIdInternal(Long id);

    Order create(Order insertedOrder);

    void updateStatus(Order order, Order.Status status);

    void updateStatusInternal(Order order, Order.Status status);

    List<LocalDate> findOccupiedDates(Long roomId);

    List<Order> findPaidLastDays(Integer numberDays);

    void cancelNotPaidOrders();

    void cancelPayRequestedOrders();
}
