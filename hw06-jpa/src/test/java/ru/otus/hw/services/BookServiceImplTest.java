package ru.otus.hw.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.JpaAuthorRepository;
import ru.otus.hw.repositories.JpaBookRepository;
import ru.otus.hw.repositories.JpaGenreRepository;

import java.util.List;

@DisplayName("Сервис книг")
@DataJpaTest
@Import({
        BookConverter.class, GenreConverter.class, AuthorConverter.class,
        JpaBookRepository.class, JpaAuthorRepository.class, JpaGenreRepository.class,
        BookServiceImpl.class
})
public class BookServiceImplTest {

    private static final long FIRST_BOOK_ID = 1L;

    @Autowired
    private BookConverter bookConverter;

    @Autowired
    private BookService bookService;

    @DisplayName("Обработка полученной книги по её id")
    @Test
    void processBookGettingById() {
        Book book = bookService.findById(FIRST_BOOK_ID).orElseThrow();
        Assertions.assertDoesNotThrow(() -> bookConverter.bookToString(book));
    }

    @DisplayName("Обработка всех книг")
    @Test
    void processAllBooks() {
        List<Book> books = bookService.findAll();
        Assertions.assertFalse(books.isEmpty());
        Assertions.assertDoesNotThrow(() -> books.forEach(b -> bookConverter.bookToString(b)));
    }

    @DisplayName("Сохранение книги")
    @Test
    void processBookInserting() {
        Book book = bookService.insert("1", 1, 1);
        Assertions.assertDoesNotThrow(() -> bookConverter.bookToString(book));
    }

    @DisplayName("Обновление книги")
    @Test
    void processBookUpdating() {
        Book book = bookService.update(1,"1", 1, 1);
        Assertions.assertDoesNotThrow(() -> bookConverter.bookToString(book));
    }
}
