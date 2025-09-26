package ru.otus.hw.rest.controllers;

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
import ru.otus.hw.mapper.PaymentMapper;
import ru.otus.hw.mapper.PaymentMapperImpl;
import ru.otus.hw.models.Payment;
import ru.otus.hw.models.User;
import ru.otus.hw.rest.GlobalExceptionHandler;
import ru.otus.hw.rest.PaymentController;
import ru.otus.hw.services.PaymentService;

import java.math.BigDecimal;
import java.util.List;

import static java.time.LocalDateTime.now;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.otus.hw.models.Payment.Status.*;

@SuppressWarnings("unused")
@DisplayName("Контроллер платежей")
@WebMvcTest(controllers = PaymentController.class)
@Import({GlobalExceptionHandler.class,
        PaymentMapperImpl.class})
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PaymentMapper paymentMapper;

    @TestConfiguration
    static class MockConfig {

        @Bean
        PaymentService paymentService() {
            return mock(PaymentService.class);
        }
    }

    @Autowired
    private PaymentService paymentService;

    @DisplayName("Положительный сценарий. Получение платежей клиента.")
    @Test
    @WithMockUser
    void findClientPayments() throws Exception {
        User user = new User(1L, "username", "pwd", "ROLE_USER", List.of(), null);
        Payment p1 = new Payment(1L, "buy1", 1L, user, new BigDecimal(1), NOT_PAID, now());
        Payment p2 = new Payment(2L, "buy2", 2L, user, new BigDecimal(2), NOT_PAID, now());
        when(paymentService.findByUsername(any())).thenReturn(List.of(p1, p2));
        mockMvc.perform(get("/api/payments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0].id").value(p1.getId()))
                .andExpect(jsonPath("$.[0].buyId").value(p1.getBuyId()))
                .andExpect(jsonPath("$.[0].username").value(p1.getUser().getUsername()))
                .andExpect(jsonPath("$.[0].price").value(p1.getPrice()))
                .andExpect(jsonPath("$.[0].status").value(p1.getStatus().name()))
                .andExpect(jsonPath("$.[1].id").value(p2.getId()))
                .andExpect(jsonPath("$.[1].buyId").value(p2.getBuyId()))
                .andExpect(jsonPath("$.[1].username").value(p2.getUser().getUsername()))
                .andExpect(jsonPath("$.[1].price").value(p2.getPrice()))
                .andExpect(jsonPath("$.[1].status").value(p2.getStatus().name()));
    }

    @DisplayName("Положительный сценарий. Проведение платежа клиента.")
    @Test
    @WithMockUser
    void pay() throws Exception {
        User user = new User(1L, "user", "pwd", "ROLE_USER", List.of(), null);
        Payment notPaidPayment = new Payment(1L, "buy1", 1L, user, new BigDecimal(1), NOT_PAID, now());
        Payment paidPayment = new Payment(1L, "buy1", 1L, user, new BigDecimal(1), PAID, now());
        when(paymentService.findById(any())).thenReturn(notPaidPayment);
        when(paymentService.pay(any())).thenReturn(paidPayment);
        mockMvc.perform(put("/api/payments/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(paidPayment.getId()))
                .andExpect(jsonPath("$.buyId").value(paidPayment.getBuyId()))
                .andExpect(jsonPath("$.username").value(paidPayment.getUser().getUsername()))
                .andExpect(jsonPath("$.price").value(paidPayment.getPrice()))
                .andExpect(jsonPath("$.status").value(paidPayment.getStatus().name()));
    }

    @DisplayName("Положительный сценарий. Отмена платежа.")
    @Test
    @WithMockUser
    void cancel() throws Exception {
        User user = new User(1L, "user", "pwd", "ROLE_USER", List.of(), null);
        Payment notPaidPayment = new Payment(1L, "buy1", 1L, user, new BigDecimal(1), NOT_PAID, now());
        Payment paidPayment = new Payment(1L, "buy1", 1L, user, new BigDecimal(1), CANCEL, now());
        when(paymentService.findById(any())).thenReturn(notPaidPayment);
        when(paymentService.pay(any())).thenReturn(paidPayment);
        mockMvc.perform(put("/api/payments/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(paidPayment.getId()))
                .andExpect(jsonPath("$.buyId").value(paidPayment.getBuyId()))
                .andExpect(jsonPath("$.username").value(paidPayment.getUser().getUsername()))
                .andExpect(jsonPath("$.price").value(paidPayment.getPrice()))
                .andExpect(jsonPath("$.status").value(paidPayment.getStatus().name()));
    }
}