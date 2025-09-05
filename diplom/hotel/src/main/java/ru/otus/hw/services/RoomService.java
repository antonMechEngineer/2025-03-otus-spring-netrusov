package ru.otus.hw.services;

import ru.otus.hw.models.Room;

import java.util.List;

public interface RoomService {

    List<Room> findAll();

    Room findById(long id);

}
