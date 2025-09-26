package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("unused")
@DisplayName("Репозиторий пользователей")
@DataJpaTest
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
@Transactional
public class JpaUserRepositoryTest {

    @Autowired
    private UserRepository jpaUserRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("Положительный кейс. Поиск пользователя по его имени")
    @Test
    void findUserByUsername() {
        var expectedUsername = "testUsername";
        var savedUser = new User(null, expectedUsername, "testPassword", "ROLE_USER", List.of());
        var expectedUser = testEntityManager.persist(savedUser);
        var actualUser = jpaUserRepository.findByUsername(expectedUsername).orElseThrow();
        assertEquals(expectedUser, actualUser);
    }
}