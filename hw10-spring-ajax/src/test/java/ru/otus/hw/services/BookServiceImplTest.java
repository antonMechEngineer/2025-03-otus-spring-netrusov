package ru.otus.hw.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ru.otus.hw.models.Book;

@DisplayName("Сервис книг")
@DataJpaTest
@Import({BookServiceImpl.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class BookServiceImplTest {

    private static final long FIRST_BOOK_ID = 1L;

    @Autowired
    private BookService bookService;

    @DisplayName("Проверка безопасности ленивого поля - автора")
    @Test
    void checkLazyFieldAuthorBookGettingById() {
        Book book = bookService.findById(FIRST_BOOK_ID).orElseThrow();
        Assertions.assertDoesNotThrow(book::getAuthor);
    }

    @DisplayName("Проверка безопасности ленивого поля - жанра")
    @Test
    void checkLazyFieldGenreBookGettingById() {
        Book book = bookService.findById(FIRST_BOOK_ID).orElseThrow();
        Assertions.assertDoesNotThrow(book::getGenre);
    } 

    @DisplayName("Проверка безопасности equals")
    @Test
    void checkEqualsBookGettingById() {
        Book book = bookService.findById(FIRST_BOOK_ID).orElseThrow();
        Assertions.assertDoesNotThrow(() -> book.equals(book));
    }

    @DisplayName("Проверка безопасности hashcode")
    @Test
    void checkHashCodeBookGettingById() {
        Book book = bookService.findById(FIRST_BOOK_ID).orElseThrow();
        Assertions.assertDoesNotThrow(book::hashCode);
    }

    @DisplayName("Проверка безопасности toString")
    @Test
    void checkToStringBookGettingById() {
        Book book = bookService.findById(FIRST_BOOK_ID).orElseThrow();
        Assertions.assertDoesNotThrow(book::toString);
    }
}
