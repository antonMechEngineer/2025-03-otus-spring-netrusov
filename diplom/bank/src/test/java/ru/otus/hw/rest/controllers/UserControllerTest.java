package ru.otus.hw.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import ru.otus.hw.mapper.UserMapper;
import ru.otus.hw.mapper.UserMapperImpl;
import ru.otus.hw.models.User;
import ru.otus.hw.rest.GlobalExceptionHandler;
import ru.otus.hw.rest.UserController;
import ru.otus.hw.rest.dto.UserDto;
import ru.otus.hw.services.UserService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("unused")
@DisplayName("Контроллер пользователей")
@WebMvcTest(controllers = UserController.class)
@Import({GlobalExceptionHandler.class,
        UserMapperImpl.class})
class UserControllerTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserMapper userMapper;

    @TestConfiguration
    static class MockConfig {

        @Bean
        UserService userService() {
            return mock(UserService.class);
        }
    }

    @Autowired
    private UserService userService;

    @DisplayName("Положительный сценарий. Получение информации о профиле.")
    @Test
    @WithMockUser
    void findCurrent() throws Exception {
        User user = new User(1L, "username", "pwd", "ROLE_USER", List.of(), null);
        when(userService.findByUsername(any())).thenReturn(user);
        mockMvc.perform(get("/api/profile"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("username"))
                .andExpect(jsonPath("$.password").value("pwd"));
    }


    @DisplayName("Положительный сценарий. Редактирование информации о профиле.")
    @Test
    @WithMockUser
    void edit() throws Exception {
        String usernameDto = "usernameFromDto";
        String pwdDto = "pwdFromDto";
        String roleDto = "ROLE_USER";
        User userFromService = new User(1L, "user", "pwd", "ROLE_USER", List.of(), null);
        User userModified = new User(1L, usernameDto, pwdDto, "ROLE_USER", List.of(), null);
        UserDto userDto = new UserDto(1L, usernameDto, pwdDto);
        when(userService.findByUsername(any())).thenReturn(userFromService);
        when(userService.edit(any())).thenReturn(userModified);
        mockMvc.perform(put("/api/profile")
                        .content(OBJECT_MAPPER.writeValueAsBytes(userDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.username").value(usernameDto))
                .andExpect(jsonPath("$.password").value(pwdDto));
    }
}