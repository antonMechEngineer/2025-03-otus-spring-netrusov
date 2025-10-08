package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Room;
import ru.otus.hw.repositories.RoomRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.otus.hw.models.Room.Type.LUX;
import static ru.otus.hw.models.Room.Type.STANDARD;

@ExtendWith(MockitoExtension.class)
@DisplayName("Сервис заказов")
public class RoomServiceImplTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomServiceImpl roomService;

    private Room room;

    @BeforeEach
    void setUp() {
        room = new Room(1L, 999, LUX, new BigDecimal("1000.0"), List.of());
    }

    @DisplayName("Положительный сценарий. Проверка поиска комнаты по id.")
    @Test
    void findByIdPositive() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        roomService.findById(1L);
        verify(roomRepository, times(1)).findById(1L);
    }

    @DisplayName("Отрицательный сценарий. Проверка поиска несуществующей комнаты по id.")
    @Test
    void findByIdNegativeNotFound() {
        when(roomRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> roomService.findById(999L));
        verify(roomRepository, times(1)).findById(999L);
    }


    @DisplayName("Положительный сценарий. Проверка создания комнаты.")
    @Test
    void createPositive() {
        when(roomRepository.save(any(Room.class))).thenReturn(room);
        roomService.save(room);
        verify(roomRepository, times(1)).save(room);
    }


    @DisplayName("Положительный сценарий. Проверка редактирования команты.")
    @Test
    void updatePositive() {
        var updatedId = 1L;
        var updatedType = STANDARD;
        var updatedPrice = new BigDecimal("1500.0");
        var updatedRoomNumber = 2;
        var updatedRoom = new Room(updatedId, updatedRoomNumber, updatedType, updatedPrice, List.of());
        when(roomRepository.findById(updatedId)).thenReturn(Optional.of(room));
        when(roomRepository.save(any(Room.class))).thenReturn(updatedRoom);
        var result = roomService.update(updatedId, updatedRoom);
        verify(roomRepository, times(1)).findById(updatedId);
        verify(roomRepository, times(1)).save(room);
        assertEquals(updatedPrice, result.getPricePerDay());
        assertEquals(updatedRoomNumber, result.getRoomNumber());
        assertEquals(updatedType, result.getType());
    }


    @DisplayName("Положительный сценарий. Проверка удаления комнаты по id.")
    @Test
    void deletePositive() {
        var deletedRoomId = 1L;
        when(roomRepository.findById(deletedRoomId)).thenReturn(Optional.of(room));
        roomService.deleteById(deletedRoomId);
        verify(roomRepository, times(1)).findById(deletedRoomId);
        verify(roomRepository, times(1)).delete(room);
    }
}