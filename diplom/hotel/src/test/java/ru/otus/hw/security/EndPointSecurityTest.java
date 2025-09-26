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
import ru.otus.hw.mapper.*;
import ru.otus.hw.models.Order;
import ru.otus.hw.models.Room;
import ru.otus.hw.models.User;
import ru.otus.hw.rest.GlobalExceptionHandler;
import ru.otus.hw.services.OrderService;
import ru.otus.hw.services.RoomService;
import ru.otus.hw.services.UserService;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static java.time.LocalDate.now;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.otus.hw.models.Room.Type.LUX;

@SuppressWarnings("unused")
@DisplayName("Security-тесты для эндпоинтов Hotel")
@WebMvcTest
@Import({SecurityConfiguration.class,
        GlobalExceptionHandler.class,
        UserMapperImpl.class,
        OrderMapperImpl.class,
        RoomMapperImpl.class})
class EndPointSecurityTest {

    private static final Room ROOM_LUX = new Room(1L, 1, LUX, new BigDecimal("1000.0"), List.of());

    private static final User USER = new User(null, "testUsername", "testPassword", "ROLE_USER", List.of());

    private static final Order ORDER = new Order(now(), now().plusDays(1), USER, ROOM_LUX);

    @Autowired
    private MockMvc mvc;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private UserMapper userMapper;


    @TestConfiguration
    static class MockConfig {

        @Bean
        RoomService roomService() {
            return mock(RoomService.class);
        }

        @Bean
        OrderService orderService() {
            return mock(OrderService.class);
        }

        @Bean
        UserService userService() {
            return mock(UserService.class);
        }

        @Bean
        public ObjectMapper objectMapper() {
            var mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return mapper;
        }
    }

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private UserService userService;

    static Stream<Arguments> publicEndpoints() {
        return Stream.of(
                Arguments.of(GET, "/rooms"),
                Arguments.of(GET, "/api/rooms")
        );
    }

    static Stream<Arguments> adminEndpoints() {
        return Stream.of(
                Arguments.of(GET, "/createRoom"),
                Arguments.of(GET, "/editRoom"),
                Arguments.of(POST, "/api/rooms"),
                Arguments.of(PUT, "/api/rooms/1"),
                Arguments.of(DELETE, "/api/rooms/1")
        );
    }

    static Stream<Arguments> authenticatedEndpoints() {
        return Stream.of(
                Arguments.of(GET, "/orders"),
                Arguments.of(GET, "/createOrder"),
                Arguments.of(GET, "/profile"),
                Arguments.of(GET, "/editProfile"),
                Arguments.of(GET, "/api/orders"),
                Arguments.of(GET, "/api/orders/1"),
                Arguments.of(POST, "/api/orders/unconfirmed"),
                Arguments.of(PUT, "/api/orders/1/pay"),
                Arguments.of(PUT, "/api/orders/1/cancel"),
                Arguments.of(GET, "/api/profile")
        );
    }

    @DisplayName("Положительный сценарий. Публичные эндпоинты доступны без авторизации")
    @ParameterizedTest
    @MethodSource("publicEndpoints")
    void shouldPermitAllWithoutAuth(HttpMethod method, String url) throws Exception {
        mvc.perform(request(method, url))
                .andExpect(status().isOk());
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
        when(orderService.create(any())).thenReturn(ORDER);
        when(roomService.findById(any())).thenReturn(ROOM_LUX);
        when(userService.findCurrent()).thenReturn(USER);
        mvc.perform(request(method, url)
                        .param("id", "1")
                        .content(objectMapper.writeValueAsBytes(orderMapper.toDto(ORDER)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @DisplayName("Отрицательный сценарий. ADMIN-эндпоинты недоступны без авторизации")
    @ParameterizedTest
    @MethodSource("adminEndpoints")
    void shouldRedirectAdminEndpointsForAnonymous(HttpMethod method, String url) throws Exception {
        mvc.perform(request(method, url))
                .andExpect(status().is3xxRedirection());
    }

    @DisplayName("Отрицательный сценарий. ADMIN-эндпоинты запрещены пользователям без роли ADMIN")
    @WithMockUser
    @ParameterizedTest
    @MethodSource("adminEndpoints")
    void shouldForbiddenForNonAdmin(HttpMethod method, String url) throws Exception {
        mvc.perform(request(method, url)
                        .content(objectMapper.writeValueAsBytes(roomMapper.toDto(ROOM_LUX)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Отрицательный сценарий. ADMIN-эндпоинты доступны пользователям с ролью ADMIN")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @ParameterizedTest
    @MethodSource("adminEndpoints")
    void shouldAllowForAdmin(HttpMethod method, String url) throws Exception {
        mvc.perform(request(method, url)
                        .param("id", "1")
                        .content(objectMapper.writeValueAsBytes(roomMapper.toDto(ROOM_LUX)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }
}
