package ru.otus.hw.rest;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.mapper.RoomMapper;
import ru.otus.hw.rest.dto.RoomDto;
import ru.otus.hw.services.OrderService;
import ru.otus.hw.services.RoomService;

import java.time.LocalDate;
import java.util.List;

@SuppressWarnings("unused")
@RequiredArgsConstructor
@RestController
public class RoomController {

    private final RoomService roomService;

    private final OrderService orderService;

    private final RoomMapper roomMapper;

    @GetMapping("/api/rooms")
    @RateLimiter(name = "roomsRateLimiter")
    public List<RoomDto> findAllRooms() {
        return roomService.findAll().stream().map(roomMapper::toDto).toList();
    }

    @GetMapping("/api/rooms/{id}")
    public ResponseEntity<RoomDto> findRoomById(@PathVariable("id") Long id) {
        List<LocalDate> occupiedDates = orderService.findOccupiedDates(id);
        RoomDto roomDto = roomMapper.toDto(roomService.findById(id));
        roomDto.setOccupiedDates(occupiedDates);
        return ResponseEntity.status(HttpStatus.CREATED).body(roomDto);
    }
}
