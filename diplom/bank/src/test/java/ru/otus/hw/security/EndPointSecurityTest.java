package ru.otus.hw.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.mapper.AccountMapperImpl;
import ru.otus.hw.mapper.PaymentMapperImpl;
import ru.otus.hw.mapper.UserMapperImpl;
import ru.otus.hw.models.Account;
import ru.otus.hw.models.Payment;
import ru.otus.hw.models.User;
import ru.otus.hw.rest.GlobalExceptionHandler;
import ru.otus.hw.services.AccountService;
import ru.otus.hw.services.PaymentService;
import ru.otus.hw.services.UserService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.otus.hw.models.Payment.Status.NOT_PAID;

@SuppressWarnings("unused")
@DisplayName("Security-тесты для эндпоинтов Bank")
@WebMvcTest
@Import({SecurityConfiguration.class,
        GlobalExceptionHandler.class,
        UserMapperImpl.class,
        PaymentMapperImpl.class,
        AccountMapperImpl.class})
class EndPointSecurityTest {

    private static final Account ACCOUNT = new Account(1L, new BigDecimal("1000.00"), null);
    private static final User USER = new User(1L, "testUser", "testPassword", "ROLE_USER", List.of(), ACCOUNT);
    private static final Payment PAYMENT = new Payment(1L, "Hotel", 100L, USER,
            new BigDecimal("200.00"), NOT_PAID, LocalDateTime.now());

    @Autowired
    private MockMvc mvc;

    @TestConfiguration
    static class MockConfig {
        @Bean
        PaymentService paymentService() {
            return mock(PaymentService.class);
        }

        @Bean
        AccountService accountService() {
            return mock(AccountService.class);
        }

        @Bean
        UserService userService() {
            return mock(UserService.class);
        }

        @Bean
        public ObjectMapper objectMapper() {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return mapper;
        }
    }

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    static Stream<Arguments> authenticatedEndpoints() {
        return Stream.of(
                Arguments.of(GET, "/payments"),
                Arguments.of(GET, "/profile"),
                Arguments.of(GET, "/editProfile"),
                Arguments.of(GET, "/api/balance"),
                Arguments.of(GET, "/api/payments"),
                Arguments.of(PUT, "/api/payments/1"),
                Arguments.of(PUT, "/api/payments/1/cancel"),
                Arguments.of(GET, "/api/profile")
        );
    }

    @DisplayName("Отрицательный сценарий. Эндпоинты требуют авторизации")
    @ParameterizedTest
    @MethodSource("authenticatedEndpoints")
    void shouldRedirectUnauthenticated(HttpMethod method, String url) throws Exception {
        mvc.perform(request(method, url))
                .andExpect(status().is3xxRedirection());
    }

    @DisplayName("Положительный сценарий. Эндпоинты доступны авторизованному пользователю")
    @WithMockUser
    @ParameterizedTest
    @MethodSource("authenticatedEndpoints")
    void shouldAllowAuthenticatedUser(HttpMethod method, String url) throws Exception {
        when(paymentService.findById(any())).thenReturn(PAYMENT);
        when(userService.findCurrentUser()).thenReturn(USER);
        when(accountService.findByUsername(any())).thenReturn(ACCOUNT);

        mvc.perform(request(method, url)
                        .param("id", "1")
                        .content(objectMapper.writeValueAsBytes(PAYMENT))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }
}