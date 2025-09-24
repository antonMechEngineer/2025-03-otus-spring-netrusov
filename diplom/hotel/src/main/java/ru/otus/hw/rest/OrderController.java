package ru.otus.hw.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.mapper.OrderMapper;
import ru.otus.hw.models.Order;
import ru.otus.hw.models.Room;
import ru.otus.hw.models.User;
import ru.otus.hw.rest.dto.OrderDto;
import ru.otus.hw.services.OrderService;
import ru.otus.hw.services.RoomService;
import ru.otus.hw.services.UserService;

import java.util.List;

@SuppressWarnings("unused")
@RequiredArgsConstructor
@RestController
public class   OrderController {

    private final RoomService roomService;

    private final OrderService orderService;

    private final UserService userService;

    private final OrderMapper orderMapper;

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderDto>> findAll(Authentication authentication) {
        List<OrderDto> orderDtos = orderService.findByUsername(authentication.getName())
                .stream()
                .map(orderMapper::toDto)
                .toList();
        return ResponseEntity.ok(orderDtos);
    }

    @GetMapping("/api/orders/{id}")
    public ResponseEntity<OrderDto> findById(@PathVariable("id") long id) {
        Order order = orderService.findById(id);
        OrderDto orderDto = orderMapper.toDto(order);
        return ResponseEntity.status(HttpStatus.OK).body(orderDto);
    }

    @PostMapping("/api/orders/unconfirmed")
    public ResponseEntity<OrderDto> create(@Valid @RequestBody OrderDto orderDto) {
        Room room = roomService.findById(orderDto.getId());
        User user = userService.findCurrent();
        Order order = orderService.create(new Order(orderDto.getBeginRent(), orderDto.getEndRent(), user, room));
        OrderDto createdOrderDto = orderMapper.toDto(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrderDto);
    }

    @PutMapping("/api/orders/{id}/pay")
    public ResponseEntity<Void> pay( @PathVariable("id") long id) {
        Order order = orderService.findById(id);
        orderService.updateStatus(order, Order.Status.PAYMENT_REQUEST);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/api/orders/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable("id") long id) {
        Order order = orderService.findById(id);
        orderService.updateStatus(order, Order.Status.CANCEL);
        return ResponseEntity.noContent().build();
    }
}
