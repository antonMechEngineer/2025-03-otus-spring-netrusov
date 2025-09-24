package ru.otus.hw.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import ru.otus.hw.mapper.OrderMapper;
import ru.otus.hw.mapper.OrderMapperImpl;
import ru.otus.hw.models.Order;
import ru.otus.hw.models.Room;
import ru.otus.hw.models.User;
import ru.otus.hw.rest.GlobalExceptionHandler;
import ru.otus.hw.rest.OrderController;
import ru.otus.hw.rest.dto.OrderDto;
import ru.otus.hw.services.OrderService;
import ru.otus.hw.services.RoomService;
import ru.otus.hw.services.UserService;

import java.math.BigDecimal;
import java.util.List;

import static java.time.LocalDate.now;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.otus.hw.models.Room.Type.LUX;

@SuppressWarnings("unused")
@DisplayName("Контроллер заказов")
@WebMvcTest(controllers = OrderController.class)
@Import({GlobalExceptionHandler.class,
        OrderMapperImpl.class})
class OrderControllerTest {

    private static final Room ROOM_LUX = new Room(1L, 1, LUX, new BigDecimal("1000.0"), List.of());

    private static final User USER = new User(1L, "usr", "pwd", "ROLE_USER", List.of());

    private static final Order NOT_PAID_ORDER = new Order(now(), now().plusDays(1), USER, ROOM_LUX);

    private static final Order PAID_ORDER = new Order(now().plusDays(2), now().plusDays(3), USER, ROOM_LUX);

    @Autowired
    private MockMvc mvc;

    @Autowired
    private OrderMapper orderMapper;

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
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return mapper;
        }
    }

    @Autowired
    private OrderService orderService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("Положительный сценарий. Получение информации о заказах авторизованного клиента.")
    @WithMockUser
    @Test
    void findAll() throws Exception {
        List<Order> expectedOrders = List.of(NOT_PAID_ORDER, PAID_ORDER);
        when(orderService.findByUsername(any())).thenReturn(expectedOrders);
        mvc.perform(get("/api/orders"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0].id").value(NOT_PAID_ORDER.getId()))
                .andExpect(jsonPath("$.[0].beginRent").value(NOT_PAID_ORDER.getBeginRent().toString()))
                .andExpect(jsonPath("$.[0].endRent").value(NOT_PAID_ORDER.getEndRent().toString()))
                .andExpect(jsonPath("$.[0].username").value(NOT_PAID_ORDER.getUser().getUsername()))
                .andExpect(jsonPath("$.[0].roomNumber").value(NOT_PAID_ORDER.getRoom().getRoomNumber()))
                .andExpect(jsonPath("$.[0].totalPrice").value(NOT_PAID_ORDER.getTotalPrice()))
                .andExpect(jsonPath("$.[1].id").value(PAID_ORDER.getId()))
                .andExpect(jsonPath("$.[1].beginRent").value(PAID_ORDER.getBeginRent().toString()))
                .andExpect(jsonPath("$.[1].endRent").value(PAID_ORDER.getEndRent().toString()))
                .andExpect(jsonPath("$.[1].username").value(PAID_ORDER.getUser().getUsername()))
                .andExpect(jsonPath("$.[1].roomNumber").value(PAID_ORDER.getRoom().getRoomNumber()))
                .andExpect(jsonPath("$.[1].totalPrice").value(PAID_ORDER.getTotalPrice()));
    }

    @DisplayName("Положительный сценарий. Получение информации о заказе по id.")
    @WithMockUser
    @Test
    void findById() throws Exception {
        when(orderService.findById(anyLong())).thenReturn(NOT_PAID_ORDER);
        mvc.perform(get("/api/orders/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(NOT_PAID_ORDER.getId()))
                .andExpect(jsonPath("$.beginRent").value(NOT_PAID_ORDER.getBeginRent().toString()))
                .andExpect(jsonPath("$.endRent").value(NOT_PAID_ORDER.getEndRent().toString()))
                .andExpect(jsonPath("$.username").value(NOT_PAID_ORDER.getUser().getUsername()))
                .andExpect(jsonPath("$.roomNumber").value(NOT_PAID_ORDER.getRoom().getRoomNumber()))
                .andExpect(jsonPath("$.totalPrice").value(NOT_PAID_ORDER.getTotalPrice()));
    }

    @DisplayName("Положительный сценарий. Отмена заказа.")
    @WithMockUser
    @Test
    void create() throws Exception {
        OrderDto orderDto = orderMapper.toDto(NOT_PAID_ORDER);
        when(orderService.create(any())).thenReturn(NOT_PAID_ORDER);
        when(roomService.findById(any())).thenReturn(ROOM_LUX);
        when(userService.findCurrent()).thenReturn(USER);
        mvc.perform(post("/api/orders/unconfirmed")
                        .content(objectMapper.writeValueAsBytes(orderDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(NOT_PAID_ORDER.getId()))
                .andExpect(jsonPath("$.beginRent").value(NOT_PAID_ORDER.getBeginRent().toString()))
                .andExpect(jsonPath("$.endRent").value(NOT_PAID_ORDER.getEndRent().toString()))
                .andExpect(jsonPath("$.username").value(NOT_PAID_ORDER.getUser().getUsername()))
                .andExpect(jsonPath("$.roomNumber").value(NOT_PAID_ORDER.getRoom().getRoomNumber()))
                .andExpect(jsonPath("$.totalPrice").value(NOT_PAID_ORDER.getTotalPrice()));
    }

    @DisplayName("Положительный сценарий. Оплата заказа.")
    @WithMockUser
    @Test
    void pay() throws Exception {
        mvc.perform(put("/api/orders/1/pay")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @DisplayName("Положительный сценарий. Отмена заказа.")
    @WithMockUser
    @Test
    void cancel() throws Exception {
        mvc.perform(put("/api/orders/1/cancel")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}