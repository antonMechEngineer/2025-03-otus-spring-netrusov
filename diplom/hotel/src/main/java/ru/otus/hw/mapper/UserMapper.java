package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw.models.User;
import ru.otus.hw.rest.dto.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    User fromDto(UserDto userDto);

}
