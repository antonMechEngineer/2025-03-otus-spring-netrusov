package ru.otus.hw.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;

@DisplayName("Сервис комментариев")
@DataMongoTest
@Import({CommentServiceImpl.class})
public class CommentServiceImplTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CommentService commentService;

    private Book book;

    @BeforeEach
    void cleanDatabase() {
        mongoTemplate.getDb().drop();
        Genre genre = new Genre("1", "123");
        mongoTemplate.save(genre);
        Author author = new Author("1", "123");
        mongoTemplate.save(author);
        book = new Book("1", "123", author, genre);
        mongoTemplate.save(book);
    }

    @DisplayName("загрузка комментария по id")
    @Test
    void findCommentById() {
        var expectedComment = mongoTemplate.save(new Comment("1", "123", book));
        var actualComment = commentService.findById(1).orElseThrow();
        Assertions.assertEquals(expectedComment, actualComment);
    }

    @DisplayName("загрузка всех комментариев к книге")
    @Test
    void findCommentByBook() {
        List<Comment> expectedAllComments = List.of(new Comment("1", "123", book));
        expectedAllComments.forEach(c -> mongoTemplate.save(c));
        List<Comment> actualAllComments = commentService.findByBook(Long.parseLong(book.getId()));
        Assertions.assertEquals(expectedAllComments, actualAllComments);
    }

    @DisplayName("сохранение нового комментария")
    @Test
    void insertComment() {
        String expectedPayload = "123";
        String expectedBookId = "1";
        commentService.insert(expectedPayload, Long.parseLong(expectedBookId));
        Comment actualComment = mongoTemplate.findAll(Comment.class).get(0);
        Assertions.assertEquals(expectedPayload, actualComment.getPayloadComment());
        Assertions.assertEquals(expectedBookId, actualComment.getBook().getId());
    }

    @DisplayName("редактирование существующего комментария")
    @Test
    void updateComment() {
        mongoTemplate.save(new Comment("1", "123", book));
        String expectedPayload = "345";
        commentService.update(1, expectedPayload, Long.parseLong(book.getId()));
        Assertions.assertEquals(expectedPayload, mongoTemplate.findAll(Comment.class).get(0).getPayloadComment());
    }

    @DisplayName("удаление существующего комментария")
    @Test
    void deleteComment() {
        mongoTemplate.save(new Comment("1", "123", book));
        commentService.deleteById(1);
        Assertions.assertTrue(mongoTemplate.findAll(Comment.class).isEmpty());
    }
}
