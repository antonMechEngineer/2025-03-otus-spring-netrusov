package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.hw.models.Order;
import ru.otus.hw.rest.dto.OrderDto;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "room.roomNumber", target = "roomNumber")
    @Mapping(source = "user.username", target = "username")
    OrderDto toDto(Order order);

    Order fromDto(OrderDto orderDto);

}
