package ru.otus.hw.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.mapper.UserMapper;
import ru.otus.hw.rest.dto.UserDto;
import ru.otus.hw.services.UserService;

@SuppressWarnings("unused")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    @GetMapping("/api/profile")
    public UserDto getCurrentUser(Authentication authentication) {
        return userMapper.toDto(userService.findByUsername(authentication.getName()));
    }

    @PutMapping("/api/profile")
    public UserDto editUser(Authentication authentication, @Valid @RequestBody UserDto userDto) {
        var currentUser = userService.findByUsername(authentication.getName());
        currentUser.setUsername(userDto.getUsername());
        currentUser.setPassword(userDto.getPassword());
        var updatedUser = userService.edit(currentUser);
        return userMapper.toDto(updatedUser);
    }
}
