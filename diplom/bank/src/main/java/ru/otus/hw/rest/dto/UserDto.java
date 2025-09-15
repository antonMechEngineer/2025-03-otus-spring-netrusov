package ru.otus.hw.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {

    private Long id;

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 1, max = 50, message = "Password must be between 1 and 50 characters")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 1, max = 100, message = "Password must be between 1 and 100 characters")
    private String password;

}
