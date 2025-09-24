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
@DisplayName("Контроллер страниц заказов")
@WebMvcTest(value = OrderPageController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class OrderPageControllerTest {

    @Autowired
    private MockMvc mvc;

    @DisplayName("Положительный случай. Получить страницу заказов.")
    @Test
    void shouldReturnAllOrdersPage() throws Exception {
        mvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders"));
    }

    @DisplayName("Положительный случай. Получить страницу создания заказов.")
    @Test
    void shouldReturnCreateOrderPage() throws Exception {
        mvc.perform(get("/createOrder"))
                .andExpect(status().isOk())
                .andExpect(view().name("createOrder"));
    }
}