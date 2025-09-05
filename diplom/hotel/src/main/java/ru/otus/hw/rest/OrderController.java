package ru.otus.hw.rest;

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
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class OrderController {

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
    public ResponseEntity<OrderDto> findById(Authentication authentication, @PathVariable Long id) {
        Order order = orderService.findById(id);
        if (!order.getUser().getUsername().equals(authentication.getName())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(orderMapper.toDto(order));
    }

    @PostMapping("/api/orders/unconfirmed")
    public ResponseEntity<OrderDto> create(@RequestBody OrderDto orderDto) {
        Room room = roomService.findById(orderDto.getId());
        User user = userService.findCurrentUser();
        Order order = orderService.create(new Order(orderDto.getBeginRent(), orderDto.getEndRent(), user, room));
        OrderDto createdOrderDto = orderMapper.toDto(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrderDto);
    }

    @PutMapping("/api/orders/{id}/pay")
    public ResponseEntity<OrderDto> pay(Authentication authentication, @PathVariable long id) {
        Order order = orderService.findById(id);
        if (!order.getUser().getUsername().equals(authentication.getName())) {
            return ResponseEntity.notFound().build();
        }
        orderService.updateStatus(id, Order.Status.PAYMENT_REQUEST);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/api/orders/{id}/cancel")
    public ResponseEntity<Void> cancel(Authentication authentication, @PathVariable Long id) {
        if (!orderService.findById(id).getUser().getUsername().equals(authentication.getName())) {
            return ResponseEntity.notFound().build();
        }
        orderService.updateStatus(id, Order.Status.CANCEL);
        return ResponseEntity.noContent().build();
    }
}
