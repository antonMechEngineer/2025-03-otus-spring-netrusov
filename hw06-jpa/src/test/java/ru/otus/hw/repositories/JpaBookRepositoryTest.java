package ru.otus.hw.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Book;

import java.util.List;

@DisplayName("Репозиторий книг")
@DataJpaTest
@Import(JpaBookRepository.class)
public class JpaBookRepositoryTest {

    private static final long FIRST_BOOK_ID = 1L;

    @Autowired
    private JpaBookRepository jpaBookRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("загрузка книги по id")
    @Test
    void findBookById() {
        Book actualBook = jpaBookRepository.findById(FIRST_BOOK_ID).orElseThrow();
        Book expectedBook = testEntityManager.find(Book.class, FIRST_BOOK_ID);
        Assertions.assertEquals(expectedBook, actualBook);
    }

    @DisplayName("загрузка всех книг")
    @Test
    void findAllBooks() {
        List<Book> actualBooks = jpaBookRepository.findAll();
        List<Book> expectedBooks = testEntityManager.getEntityManager().createQuery("SELECT b FROM Book b", Book.class).getResultList();
        Assertions.assertEquals(expectedBooks, actualBooks);
    }

    @DisplayName("сохранение новой книги")
    @Test
    void insertBook() {
        String insertedTitle = "savedBook";
        Book savedBook = new Book();
        savedBook.setTitle(insertedTitle);
        jpaBookRepository.save(savedBook);
        List<Book> allBooks = testEntityManager.getEntityManager().createQuery("SELECT b FROM Book b", Book.class).getResultList();
        Assertions.assertTrue(allBooks.stream().anyMatch(b -> b.getTitle().equals(insertedTitle)));
    }

    @DisplayName("редактирование существующей книги")
    @Test
    void updateBook() {
        String updatedTitle = "updatedBook";
        Book book = testEntityManager.find(Book.class, FIRST_BOOK_ID);
        int numberBooksBeforeUpdate = testEntityManager.getEntityManager().createQuery("SELECT b FROM Book b", Book.class).getResultList().size();
        book.setTitle(updatedTitle);
        jpaBookRepository.save(book);
        List<Book> allBooks = testEntityManager.getEntityManager().createQuery("SELECT b FROM Book b", Book.class).getResultList();
        Assertions.assertTrue(allBooks.stream().anyMatch(b -> b.getTitle().equals(updatedTitle)));
        Assertions.assertEquals(numberBooksBeforeUpdate, allBooks.size());
    }

    @DisplayName("удаление существующей книги")
    @Test
    void deleteBook() {
        int numberBooksBeforeDelete = testEntityManager.getEntityManager().createQuery("SELECT b FROM Book b", Book.class).getResultList().size();
        jpaBookRepository.deleteById(FIRST_BOOK_ID);
        List<Book> allBooks = testEntityManager.getEntityManager().createQuery("SELECT b FROM Book b", Book.class).getResultList();
        Assertions.assertFalse(allBooks.stream().anyMatch(b -> b.getId() == FIRST_BOOK_ID));
        Assertions.assertEquals(numberBooksBeforeDelete, allBooks.size() + 1);
    }

}
