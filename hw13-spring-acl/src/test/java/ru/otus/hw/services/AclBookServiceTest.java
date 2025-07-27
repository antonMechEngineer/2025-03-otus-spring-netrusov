package ru.otus.hw.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Book;

@DisplayName("Сервис проверки ACL книг")
@SpringBootTest
@Transactional
public class AclBookServiceTest {

    @Autowired
    private BookService bookService;

    @Test
    @WithMockUser(username = "def")
    void testNotFilteredSecretBook() {
        int expectedSizeWithSecretBook = 3;
        Assertions.assertEquals(expectedSizeWithSecretBook, bookService.findAll().size());
    }

    @Test
    @WithMockUser(username = "abc")
    void testFilteredSecretBook() {
        int expectedSizeWithoutSecretBook = 2;
        Assertions.assertEquals(expectedSizeWithoutSecretBook, bookService.findAll().size());
    }

    @Test
    @WithMockUser(username = "def")
    void testGrantedUpdate() {
        Book book = bookService.findById(1L).orElseThrow();
        Assertions.assertDoesNotThrow(() -> bookService.update(book));
    }

    @Test
    @WithMockUser(username = "abc")
    void testDeniedUpdate() {
        Book book = bookService.findById(1L).orElseThrow();
        Assertions.assertThrows(AuthorizationDeniedException.class, () -> bookService.update(book));
    }

    @Test
    @WithMockUser(username = "def")
    void testGrantedDelete() {
        Book book = bookService.findById(1L).orElseThrow();
        Assertions.assertDoesNotThrow(() -> bookService.delete(book));
    }

    @Test
    @WithMockUser(username = "abc")
    void testDeniedDelete() {
        Book book = bookService.findById(1L).orElseThrow();
        Assertions.assertThrows(AuthorizationDeniedException.class, () -> bookService.delete(book));
    }
}
