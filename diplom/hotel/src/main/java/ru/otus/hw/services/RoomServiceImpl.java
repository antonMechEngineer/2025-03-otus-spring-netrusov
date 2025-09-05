package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.Order;
import ru.otus.hw.models.Room;
import ru.otus.hw.repositories.OrderRepository;
import ru.otus.hw.repositories.RoomRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.time.LocalDate.now;
import static ru.otus.hw.models.Order.Status.*;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Override
    @PostFilter("hasPermission(filterObject, 'READ')")
    public List<Room> findAll() {
        return roomRepository.findAll();
    }


    @Override
    @PreAuthorize("hasPermission(#id, 'ru.otus.hw.models.Room' ,'READ')")
    public Room findById(long id) {
        return roomRepository.findById(id).orElseThrow();
    }

}
