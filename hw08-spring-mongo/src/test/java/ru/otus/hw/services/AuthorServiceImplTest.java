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

import java.util.List;

@DisplayName("Сервис комментариев")
@DataMongoTest
@Import({AuthorServiceImpl.class})
public class AuthorServiceImplTest {

    @Autowired
    private AuthorService authorService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void cleanDatabase(){
        mongoTemplate.getDb().drop();
    }

    @DisplayName("загрузка всех авторов")
    @Test
    void findAllAuthors() {
        List<Author> expectedAllAuthors = List.of(new Author("1", "author1"), new Author("2", "author2"));
        expectedAllAuthors.forEach(a -> mongoTemplate.save(a));
        List<Author> actualAllAuthors = authorService.findAll();
        Assertions.assertEquals(expectedAllAuthors, actualAllAuthors);
    }


}
