package ru.otus.hw.services;

import ru.otus.hw.models.Order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static ru.otus.hw.models.Order.Status.AUTO_CANCEL;

public interface OrderService {

    List<Order> findByUsername(String username);

    Order findById(long id);

    Order create(Order insertedOrder);

    void updateStatus(long id, Order.Status status);

    List<LocalDate> findOccupiedDates(long roomId);

    void checkAndCancelOrders();
}
