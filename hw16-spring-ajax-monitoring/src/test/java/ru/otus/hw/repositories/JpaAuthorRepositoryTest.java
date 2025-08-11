package ru.otus.hw.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.otus.hw.models.Author;

import java.util.List;

@DisplayName("Репозиторий авторов")
@DataJpaTest
@EnableJpaRepositories(basePackageClasses = AuthorRepository.class)
public class JpaAuthorRepositoryTest {
    private static final long FIRST_AUTHOR_ID = 1L;
    private static final List<Long> ALL_AUTHOR_IDS = List.of(FIRST_AUTHOR_ID, 2L, 3L);

    @Autowired
    private AuthorRepository jpaAuthorRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("загрузка автора по id")
    @Test
    void findAuthorById() {
        var actualAuthor = jpaAuthorRepository.findById(FIRST_AUTHOR_ID).orElseThrow();
        var expectedAuthor = testEntityManager.find(Author.class, FIRST_AUTHOR_ID);
        Assertions.assertEquals(expectedAuthor, actualAuthor);
    }

    @DisplayName("загрузка всех авторов")
    @Test
    void findAllAuthors() {
        var actualAuthors = jpaAuthorRepository.findAll();
        var expectedAuthors = ALL_AUTHOR_IDS.stream().map(id -> testEntityManager.find(Author.class, id)).toList();
        Assertions.assertEquals(expectedAuthors, actualAuthors);
    }
}
