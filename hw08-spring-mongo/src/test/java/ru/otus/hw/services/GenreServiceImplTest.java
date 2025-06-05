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
import ru.otus.hw.models.Genre;

import java.util.List;

@DisplayName("Сервис жанров")
@DataMongoTest
@Import({GenreServiceImpl.class})
public class GenreServiceImplTest {

    @Autowired
    private GenreService genreService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void cleanDatabase(){
        mongoTemplate.getDb().drop();
    }

    @DisplayName("загрузка всех жанров")
    @Test
    void findAllAuthors() {
        List<Genre> expectedAllGenres = List.of(new Genre(1, "genre1"), new Genre(2, "genre2"));
        expectedAllGenres.forEach(g -> mongoTemplate.save(g));
        List<Genre> actualAllGenres = genreService.findAll();
        Assertions.assertEquals(expectedAllGenres, actualAllGenres);
    }
}
