package ru.otus.hw.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Objects;

@DisplayName("Репозиторий комментариев")
@DataJpaTest
@EnableJpaRepositories(basePackageClasses = BookRepository.class)
public class JpaCommentRepositoryTest {
    private static final long FIRST_COMMENT_ID = 1L;
    private static final long SECOND_BOOK_ID = 2L;
    private static final long INSERTED_BOOK_ID = 4L;
    private static final List<Long> ALL_COMMENT_IDS = List.of(FIRST_COMMENT_ID, 2L, 3L);

    @Autowired
    private CommentRepository jpaCommentRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("загрузка автора по id")
    @Test
    void findCommentById() {
        var actualComment = jpaCommentRepository.findById(FIRST_COMMENT_ID).orElseThrow();
        var expectedComment = testEntityManager.find(Comment.class, FIRST_COMMENT_ID);
        Assertions.assertEquals(expectedComment, actualComment);
    }

    @DisplayName("загрузка всех комментариев к книге")
    @Test
    void findCommentByBook() {
        var actualComments = jpaCommentRepository.findByBookId(SECOND_BOOK_ID);
        Book expectedBook = testEntityManager.find(Book.class, SECOND_BOOK_ID);
        var expectedComments = ALL_COMMENT_IDS.stream()
                .map(id -> testEntityManager.find(Comment.class, id))
                .filter(b -> b.getBook().equals(expectedBook))
                .toList();
        Assertions.assertEquals(expectedComments, actualComments);
    }

    @DisplayName("сохранение нового комментария")
    @Test
    void insertComment() {
        String insertedPayload = "savedComment";
        Comment expectedComment = new Comment();
        expectedComment.setPayloadComment(insertedPayload);
        jpaCommentRepository.save(expectedComment);
        var actualComment = testEntityManager.find(Comment.class, INSERTED_BOOK_ID);
        Assertions.assertEquals(expectedComment, actualComment);
    }

    @DisplayName("редактирование существующего комментария")
    @Test
    void updateComment() {
        String updatedPayload = "updatedPayload";
        Comment comment = testEntityManager.find(Comment.class, FIRST_COMMENT_ID);
        comment.setPayloadComment(updatedPayload);
        jpaCommentRepository.save(comment);
        var allComments = ALL_COMMENT_IDS.stream().map(id -> testEntityManager.find(Comment.class, id)).toList();
        Assertions.assertTrue(allComments.stream().anyMatch(b -> b.getPayloadComment().equals(updatedPayload)));
    }

    @DisplayName("удаление существующей книги")
    @Test
    void deleteComment() {
        jpaCommentRepository.deleteById(FIRST_COMMENT_ID);
        var allBooks = ALL_COMMENT_IDS.stream().map(id -> testEntityManager.find(Comment.class, id)).toList();
        Assertions.assertFalse(allBooks.stream().filter(Objects::nonNull).anyMatch(b -> b.getId() == FIRST_COMMENT_ID));
    }
}
