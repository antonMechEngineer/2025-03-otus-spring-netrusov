package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.Room;
import ru.otus.hw.repositories.RoomRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Override
    @Cacheable("rooms")
    @PostFilter("hasPermission(filterObject, 'READ')")
    @CircuitBreaker(name = "dbBreaker")
    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    @Override
    @PreAuthorize("hasPermission(#id, 'ru.otus.hw.models.Room' ,'READ')")
    public Room findById(Long id) {
        return roomRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

}
