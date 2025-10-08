package ru.otus.hw.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.mappers.RoomMapper;
import ru.otus.hw.mappers.RoomMapperImpl;
import ru.otus.hw.models.Room;
import ru.otus.hw.rest.GlobalExceptionHandler;
import ru.otus.hw.rest.RoomController;
import ru.otus.hw.rest.dto.RoomDto;
import ru.otus.hw.services.OrderService;
import ru.otus.hw.services.RoomService;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.otus.hw.models.Room.Type.LUX;
import static ru.otus.hw.models.Room.Type.STANDARD;

@SuppressWarnings("unused")
@DisplayName("Контроллер комнат")
@WebMvcTest(controllers = RoomController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@Import({GlobalExceptionHandler.class,
        RoomMapperImpl.class})
class RoomControllerTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final Room ROOM_LUX = new Room(1L, 1, LUX, new BigDecimal("1000.0"), List.of());

    private static final Room ROOM_STANDARD = new Room(2L, 2, STANDARD, new BigDecimal("500.0"), List.of());

    @Autowired
    private MockMvc mvc;

    @Autowired
    private RoomMapper roomMapper;

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
    }

    @Autowired
    private OrderService orderService;

    @Autowired
    private RoomService roomService;

    @DisplayName("Положительный сценарий. Получение информации о комнатах.")
    @Test
    void findAll() throws Exception {
        var expectedRooms = List.of(ROOM_LUX, ROOM_STANDARD);
        when(roomService.findAll()).thenReturn(expectedRooms);
        mvc.perform(get("/api/rooms"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0].id").value(ROOM_LUX.getId()))
                .andExpect(jsonPath("$.[0].roomNumber").value(ROOM_LUX.getRoomNumber()))
                .andExpect(jsonPath("$.[0].type").value(ROOM_LUX.getType().name()))
                .andExpect(jsonPath("$.[0].pricePerDay").value(ROOM_LUX.getPricePerDay()))
                .andExpect(jsonPath("$.[1].id").value(ROOM_STANDARD.getId()))
                .andExpect(jsonPath("$.[1].roomNumber").value(ROOM_STANDARD.getRoomNumber()))
                .andExpect(jsonPath("$.[1].type").value(ROOM_STANDARD.getType().name()))
                .andExpect(jsonPath("$.[1].pricePerDay").value(ROOM_STANDARD.getPricePerDay()));
    }

    @DisplayName("Положительный сценарий. Получение информации о комнатe по id.")
    @Test
    void findById() throws Exception {
        when(roomService.findById(anyLong())).thenReturn(ROOM_LUX);
        mvc.perform(get("/api/rooms/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(ROOM_LUX.getId()))
                .andExpect(jsonPath("$.roomNumber").value(ROOM_LUX.getRoomNumber()))
                .andExpect(jsonPath("$.type").value(ROOM_LUX.getType().name()))
                .andExpect(jsonPath("$.pricePerDay").value(ROOM_LUX.getPricePerDay()));
    }

    @DisplayName("Положительный сценарий. Редактирование информации о комнате.")
    @Test
    void update() throws Exception {
        var roomNumberDto = 10;
        var typeDto = STANDARD;
        var pricePerDayDto = new BigDecimal(20);
        var roomModified = new Room(1L, roomNumberDto, typeDto, pricePerDayDto, List.of());
        var roomDto = new RoomDto(1L, roomNumberDto, typeDto, pricePerDayDto, List.of());
        when(roomService.update(any(), any())).thenReturn(roomModified);
        mvc.perform(put("/api/rooms/1")
                        .content(OBJECT_MAPPER.writeValueAsBytes(roomDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.roomNumber").value(roomNumberDto))
                .andExpect(jsonPath("$.type").value(typeDto.name()))
                .andExpect(jsonPath("$.pricePerDay").value(pricePerDayDto));
    }

    @DisplayName("Положительный сценарий. Создание комнаты.")
    @Test
    void create() throws Exception {
        var roomNumberDto = 10;
        var typeDto = STANDARD;
        var pricePerDayDto = new BigDecimal(20);
        var savedRoom = new Room(1L, roomNumberDto, typeDto, pricePerDayDto, List.of());
        var roomDto = new RoomDto(1L, roomNumberDto, typeDto, pricePerDayDto, List.of());
        when(roomService.save(any())).thenReturn(savedRoom);
        mvc.perform(post("/api/rooms")
                        .content(OBJECT_MAPPER.writeValueAsBytes(roomDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.roomNumber").value(roomNumberDto))
                .andExpect(jsonPath("$.type").value(typeDto.name()))
                .andExpect(jsonPath("$.pricePerDay").value(pricePerDayDto));
    }

    @DisplayName("Положительный сценарий. Удаление комнаты.")
    @Test
    void deleteRoom() throws Exception {
        mvc.perform(delete("/api/rooms/1"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}