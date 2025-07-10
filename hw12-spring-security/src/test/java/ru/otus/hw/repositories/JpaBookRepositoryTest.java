package ru.otus.hw.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Objects;

@DisplayName("Репозиторий книг")
@DataJpaTest
@EnableJpaRepositories(basePackageClasses = BookRepository.class)
public class JpaBookRepositoryTest {

    private static final long FIRST_BOOK_ID = 1L;
    private static final List<Long> ALL_BOOK_IDS = List.of(FIRST_BOOK_ID, 2L, 3L);
    private static final long INSERTED_BOOK_ID = 4L;

    @Autowired
    private BookRepository jpaBookRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("загрузка книги по id")
    @Test
    void findBookById() {
        var actualBook = jpaBookRepository.findById(FIRST_BOOK_ID).orElseThrow();
        var expectedBook = testEntityManager.find(Book.class, FIRST_BOOK_ID);
        Assertions.assertEquals(expectedBook, actualBook);
    }

    @DisplayName("загрузка всех книг")
    @Test
    void findAllBooks() {
        var actualBooks = jpaBookRepository.findAll();
        var expectedBooks = ALL_BOOK_IDS.stream().map(id -> testEntityManager.find(Book.class, id)).toList();
        Assertions.assertEquals(expectedBooks, actualBooks);
    }

    @DisplayName("сохранение новой книги")
    @Test
    void insertBook() {
        String insertedTitle = "savedBook";
        Book expectedBook = new Book();
        expectedBook.setTitle(insertedTitle);
        jpaBookRepository.save(expectedBook);
        var actualBook = testEntityManager.find(Book.class, INSERTED_BOOK_ID);
        Assertions.assertEquals(expectedBook, actualBook);
    }

    @DisplayName("редактирование существующей книги")
    @Test
    void updateBook() {
        String updatedTitle = "updatedBook";
        Book book = testEntityManager.find(Book.class, FIRST_BOOK_ID);
        book.setTitle(updatedTitle);
        jpaBookRepository.save(book);
        var allBooks = ALL_BOOK_IDS.stream().map(id -> testEntityManager.find(Book.class, id)).toList();
        Assertions.assertTrue(allBooks.stream().anyMatch(b -> b.getTitle().equals(updatedTitle)));
    }

    @DisplayName("удаление существующей книги")
    @Test
    void deleteBook() {
        jpaBookRepository.deleteById(FIRST_BOOK_ID);
        var allBooks = ALL_BOOK_IDS.stream().map(id -> testEntityManager.find(Book.class, id)).toList();
        Assertions.assertFalse(allBooks.stream().filter(Objects::nonNull).anyMatch(b -> b.getId() == FIRST_BOOK_ID));
    }
}
