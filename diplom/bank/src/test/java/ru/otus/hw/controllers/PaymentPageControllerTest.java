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
@WebMvcTest(value = PaymentPageController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class PaymentPageControllerTest {

    @Autowired
    private MockMvc mvc;

    @DisplayName("Вывод всех платежей клиента")
    @Test
    void  findClientPayments() throws Exception{
        mvc.perform(get("/payments"))
                .andExpect(status().isOk())
                .andExpect(view().name("payments"));
    }
}
