package ru.otus.hw.services;

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

    void checkAndCancelOrders();
}
