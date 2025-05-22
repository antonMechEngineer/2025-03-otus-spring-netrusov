package ru.otus.hw.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;

import java.util.List;

@DisplayName("Репозиторий авторов")
@DataJpaTest
@Import(JpaAuthorRepository.class)
public class JpaAuthorRepositoryTest {
    private static final long FIRST_AUTHOR_ID = 1L;

    @Autowired
    private JpaAuthorRepository jpaAuthorRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("загрузка автора по id")
    @Test
    void findAuthorById(){
        Author actualAuthor = jpaAuthorRepository.findById(FIRST_AUTHOR_ID).orElseThrow();
        Author expectedAuthor = testEntityManager.find(Author.class, FIRST_AUTHOR_ID);
        Assertions.assertEquals(expectedAuthor, actualAuthor);
    }

    @DisplayName("загрузка всех авторов")
    @Test
    void findAllAuthors(){
        List<Author> actualAuthors = jpaAuthorRepository.findAll();
        List<Author> expectedAuthors = testEntityManager.getEntityManager().createQuery("SELECT a FROM Author a", Author.class).getResultList();
        Assertions.assertEquals(expectedAuthors, actualAuthors);
    }
}
