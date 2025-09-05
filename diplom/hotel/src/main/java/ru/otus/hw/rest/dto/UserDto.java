package ru.otus.hw.rest.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {

    private long id;

    private String username;

    private String password;

    private List<OrderDto> orderDtos;

}
