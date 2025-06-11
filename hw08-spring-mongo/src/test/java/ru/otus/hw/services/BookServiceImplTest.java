package ru.otus.hw.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;

@DisplayName("Сервис книг")
@DataMongoTest
@Import({BookServiceImpl.class})
public class BookServiceImplTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private BookService bookService;

    private Genre genre;

    private Author author;

    @BeforeEach
    void cleanDatabase() {
        mongoTemplate.getDb().drop();
        genre = new Genre("1", "123");
        mongoTemplate.save(genre);
        author = new Author("1", "123");
        mongoTemplate.save(author);
    }

    @DisplayName("загрузка книги по id")
    @Test
    void findBookById() {
        var expectedBook = new Book("1", "123", author, genre);
        mongoTemplate.save(expectedBook);
        var actualBook = bookService.findById("1").orElseThrow();
        Assertions.assertEquals(expectedBook, actualBook);
    }

    @DisplayName("загрузка всех книг")
    @Test
    void findAllBooks() {
        List<Book> expectedAllBooks = List.of(new Book("1", "123", author, genre));
        expectedAllBooks.forEach(b -> mongoTemplate.save(b));
        List<Book> actualAllBooks = bookService.findAll();
        Assertions.assertEquals(expectedAllBooks, actualAllBooks);
    }

    @DisplayName("сохранение новой книги")
    @Test
    void insertBook() {
        String expectedTitle = "123";
        bookService.insert(expectedTitle, author.getId(), genre.getId());
        Book actualBook = mongoTemplate.findAll(Book.class).get(0);
        Assertions.assertEquals(expectedTitle, actualBook.getTitle());
    }

    @DisplayName("редактирование существующей книги")
    @Test
    void updateBook() {
        var expectedBook = new Book("1", "123", author, genre);
        mongoTemplate.save(expectedBook);
        String expectedTitle = "345";
        bookService.update("1", expectedTitle, author.getId(), genre.getId());
        Assertions.assertEquals(expectedTitle, mongoTemplate.findAll(Book.class).get(0).getTitle());
    }

    @DisplayName("удаление существующей книги")
    @Test
    void deleteBook() {
        var expectedBook = new Book("1", "123", author, genre);
        mongoTemplate.save(expectedBook);
        bookService.deleteById("1");
        Assertions.assertTrue(mongoTemplate.findAll(Book.class).isEmpty());
    }

    @DisplayName("поддержка целосности комментариев")
    @Test
    void deleteBookWithConsistency() {
        var book = new Book("1", "123", author, genre);
        mongoTemplate.save(book);
        mongoTemplate.save(new Comment("1", "123", book));
        bookService.deleteById("1");
        Assertions.assertTrue(mongoTemplate.findAll(Comment.class).isEmpty());
    }

    @DisplayName("Extra: дублирование сохранения новой книги без выброса исключения")
    @Test
    void doubleSaveBookWithoutException() {
        String expectedTitle = "123";
        bookService.insert(expectedTitle, author.getId(), genre.getId());
        bookService.insert(expectedTitle + "4", author.getId(), genre.getId());
        Book actualBook = mongoTemplate.findAll(Book.class).get(0);
        Assertions.assertEquals(expectedTitle, actualBook.getTitle());
    }

    @DisplayName("Extra: неудачное дублирование сохранение новой книги с выбросом исключения")
    @Test
    void doubleSaveBookWithException() {
        String expectedTitle = "123";
        Book firstBookWithSameId = new Book("0", expectedTitle, author, genre);
        Book secondBookWithSameId = new Book("0", expectedTitle + "4", author, genre);
        mongoTemplate.insert(firstBookWithSameId);
        Assertions.assertThrows(DuplicateKeyException.class, () -> mongoTemplate.insert(secondBookWithSameId));
    }
}
