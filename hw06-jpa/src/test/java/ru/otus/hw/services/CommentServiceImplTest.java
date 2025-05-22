package ru.otus.hw.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.JpaCommentRepository;

import java.util.List;

@DisplayName("Сервис комментариев")
@DataJpaTest
@Import({CommentConverter.class, CommentServiceImpl.class, JpaCommentRepository.class})
public class CommentServiceImplTest {

    private static final long FIRST_COMMENT_ID = 1L;
    private static final long FIRST_BOOK_ID = 1L;

    @Autowired
    private CommentConverter commentConverter;

    @Autowired
    private CommentService commentService;

    @DisplayName("Обработка полученного комментария по его id")
    @Test
    void processCommentGettingById() {
        Comment comment = commentService.findById(FIRST_COMMENT_ID).orElseThrow();
        Assertions.assertDoesNotThrow(() -> commentConverter.commentToString(comment));
    }

    @DisplayName("Обработка списка комментариев для книги")
    @Test
    void processCommentsByBookId() {
        List<Comment> comments = commentService.findByBook(FIRST_BOOK_ID);
        Assertions.assertFalse(comments.isEmpty());
        Assertions.assertDoesNotThrow(() -> comments.forEach(c -> commentConverter.commentToString(c)));
    }
}
