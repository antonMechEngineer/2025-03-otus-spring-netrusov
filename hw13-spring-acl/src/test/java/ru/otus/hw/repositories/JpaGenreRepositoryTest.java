package ru.otus.hw.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.otus.hw.models.Genre;

import java.util.List;

@DisplayName("Репозиторий жанров")
@DataJpaTest
@EnableJpaRepositories(basePackageClasses = GenreRepository.class)
public class JpaGenreRepositoryTest {
    private static final long FIRST_GENRE_ID = 1L;
    private static final List<Long> ALL_GENRE_IDS = List.of(FIRST_GENRE_ID, 2L, 3L);

    @Autowired
    private GenreRepository jpaGenreRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("загрузка жанра по id")
    @Test
    void findAuthorById(){
        var actualGenre = jpaGenreRepository.findById(FIRST_GENRE_ID).orElseThrow();
        var expectedGenre = testEntityManager.find(Genre.class, FIRST_GENRE_ID);
        Assertions.assertEquals(expectedGenre, actualGenre);
    }

    @DisplayName("загрузка всех авторов")
    @Test
    void findAllAuthors(){
        var actualGenres = jpaGenreRepository.findAll();
        var expectedAuthors = ALL_GENRE_IDS.stream().map(id -> testEntityManager.find(Genre.class, id)).toList();
        Assertions.assertEquals(expectedAuthors, actualGenres);
    }
}
