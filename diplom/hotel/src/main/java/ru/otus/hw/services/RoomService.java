package ru.otus.hw.services;

import ru.otus.hw.models.Room;

import java.util.List;

public interface RoomService {

    List<Room> findAll();

    Room findById(Long id);

    Room save(Room room);

    Room update(Long id, Room room);

    void deleteById(Long id);
}
