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
import ru.otus.hw.mapper.AccountMapper;
import ru.otus.hw.mapper.AccountMapperImpl;
import ru.otus.hw.models.Account;
import ru.otus.hw.models.User;
import ru.otus.hw.rest.AccountController;
import ru.otus.hw.rest.GlobalExceptionHandler;
import ru.otus.hw.services.AccountService;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("unused")
@DisplayName("Контроллер счетов")
@WebMvcTest(controllers = AccountController.class)
@Import({GlobalExceptionHandler.class,
        AccountMapperImpl.class})
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountMapper accountMapper;

    @TestConfiguration
    static class MockConfig {

        @Bean
        AccountService accountService() {
            return mock(AccountService.class);
        }
    }

    @Autowired
    private AccountService accountService;

    @DisplayName("Положительный сценарий. Получение счёта пользователя.")
    @Test
    @WithMockUser
    void findClientBalance() throws Exception {
        var user = new User(1L, "user", "pwd", "ROLE_USER", List.of(), null);
        var account = new Account(1L, new BigDecimal("1000.0"), user);
        when(accountService.findByUsername(any())).thenReturn(account);
        mockMvc.perform(get("/api/balance"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(account.getId()))
                .andExpect(jsonPath("$.balance").value(account.getBalance()))
                .andExpect(jsonPath("$.username").value(account.getUser().getUsername()));
    }
}