package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw.models.Room;
import ru.otus.hw.rest.dto.RoomDto;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    RoomDto toDto(Room room);

    Room fromDto(RoomDto roomDto);

}
