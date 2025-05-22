package ru.otus.hw.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;

import java.util.List;

@DisplayName("Репозиторий жанров")
@DataJpaTest
@Import(JpaGenreRepository.class)
public class JpaGenreRepositoryTest {
    private static final long FIRST_GENRE_ID = 1L;

    @Autowired
    private JpaGenreRepository jpaGenreRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("загрузка жанра по id")
    @Test
    void findAuthorById(){
        Genre actualGenre = jpaGenreRepository.findById(FIRST_GENRE_ID).orElseThrow();
        Genre expectedGenre = testEntityManager.find(Genre.class, FIRST_GENRE_ID);
        Assertions.assertEquals(expectedGenre, actualGenre);
    }

    @DisplayName("загрузка всех авторов")
    @Test
    void findAllAuthors(){
        List<Genre> actualGenres = jpaGenreRepository.findAll();
        List<Genre> expectedAuthors = testEntityManager.getEntityManager().createQuery("SELECT g FROM Genre g", Genre.class).getResultList();
        Assertions.assertEquals(expectedAuthors, actualGenres);
    }

}
