package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByStatusAndCreatedAtLessThanEqual(Order.Status status, LocalDateTime timeThreshold);

    List<Order> findByStatusAndCreatedAtGreaterThanEqual(Order.Status status, LocalDateTime timeThreshold);

    boolean existsByRoomIdAndStatusInAndBeginRentLessThanEqualAndEndRentGreaterThanEqual(
            Long roomId,
            Collection<Order.Status> statuses,
            LocalDate endRent,
            LocalDate beginRent
    );
}
