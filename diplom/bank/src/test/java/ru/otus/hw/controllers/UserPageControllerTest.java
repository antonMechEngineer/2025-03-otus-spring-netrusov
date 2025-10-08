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
@DisplayName("Контроллер страниц профиля")
@WebMvcTest(value = UserPageController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class UserPageControllerTest {

    @Autowired
    private MockMvc mvc;

    @DisplayName("Положительный случай. Получить страницу профиля.")
    @Test
    void shouldReturnProfilePage() throws Exception {
        mvc.perform(get("/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"));
    }

    @DisplayName("Положительный случай. Получить страницу редактирования профиля.")
    @Test
    void shouldReturnEditProfilePage() throws Exception {
        mvc.perform(get("/editProfile").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("editProfile"));
    }
}
