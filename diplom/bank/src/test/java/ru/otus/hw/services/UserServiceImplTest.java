package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.User;
import ru.otus.hw.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Сервис пользователей")
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(null, "testUsername", "testPassword", "ROLE_USER", List.of(), null);
    }

    @DisplayName("Положительный сценарий. Поиск пользователя по имени.")
    @Test
    void findByUsernamePositive() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        userService.findByUsername(user.getUsername());
        verify(userRepository, times(1)).findByUsername(user.getUsername());
    }

    @DisplayName("Отрицательный сценарий. Поиск несуществующего пользователя по имени.")
    @Test
    void findByUsernameNegativeNotFound() {
        String notExistenceUsername = "any";
        when(userRepository.findByUsername(notExistenceUsername)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.findByUsername(notExistenceUsername));
        verify(userRepository, times(1)).findByUsername(notExistenceUsername);
    }

    @DisplayName("Положительный сценарий. Редактирование информации пользователя с шифрованием пароля.")
    @Test
    void editPositive() {
        User updatedUser = new User(null, "test", "test", "ROLE_USER", List.of(), null);
        when(bCryptPasswordEncoder.encode(updatedUser.getPassword())).thenReturn("encryptedPassword");
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);
        User result = userService.edit(updatedUser);
        assertEquals("encryptedPassword", result.getPassword());
        verify(bCryptPasswordEncoder, times(1)).encode("test");
        verify(userRepository, times(1)).save(updatedUser);
    }
}
