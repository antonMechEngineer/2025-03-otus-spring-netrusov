package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Room;
import ru.otus.hw.repositories.RoomRepository;

import java.util.List;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private static final String ERROR_NOT_FOUND = "Room id = %s not found!";

    private final RoomRepository roomRepository;

    @Cacheable(value = "rooms")
    @CircuitBreaker(name = "dbBreaker")
    @Override
    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    @Cacheable(value = "rooms", key = "#id")
    @Override
    public Room findById(Long id) {
        return roomRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(format(ERROR_NOT_FOUND, id)));
    }

    @CachePut(value = "rooms", key = "#result.id")
    @CacheEvict(value = "rooms", allEntries = true)
    @Override
    public Room save(Room room) {
        return roomRepository.save(room);
    }

    @CachePut(value = "rooms", key = "#result.id")
    @CacheEvict(value = "rooms", allEntries = true)
    @Override
    public Room update(Long id, Room room) {
        var existingRoom = findById(id);
        existingRoom.setRoomNumber(room.getRoomNumber());
        existingRoom.setType(room.getType());
        existingRoom.setPricePerDay(room.getPricePerDay());
        return roomRepository.save(existingRoom);
    }

    @CacheEvict(value = "rooms", key = "#id", allEntries = true)
    @Override
    public void deleteById(Long id) {
        var room = findById(id);
        roomRepository.delete(room);
    }
}
