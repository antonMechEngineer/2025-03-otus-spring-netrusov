package ru.otus.hw.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SuppressWarnings("unused")
@DisplayName("Контроллер страниц комнат")
@WebMvcTest(value = RoomPageController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class RoomPageControllerTest {

    @Autowired
    private MockMvc mvc;

    @DisplayName("Положительный случай. Получить страницу комнат.")
    @Test
    void shouldReturnAllRoomsPage() throws Exception {
        mvc.perform(get("/rooms"))
                .andExpect(status().isOk())
                .andExpect(view().name("rooms"));
    }

    @DisplayName("Положительный случай. Получить страницу создания комнаты.")
    @Test
    void shouldReturnCreateRoomPage() throws Exception {
        mvc.perform(get("/createRoom"))
                .andExpect(status().isOk())
                .andExpect(view().name("createRoom"));
    }

    @DisplayName("Положительный случай. Получить страницу реадктирования комнаты.")
    @Test
    void shouldReturnEditRoomPage() throws Exception {
        mvc.perform(get("/editRoom").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("editRoom"));
    }
}
