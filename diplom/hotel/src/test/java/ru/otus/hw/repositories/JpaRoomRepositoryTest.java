package ru.otus.hw.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Room;

import java.math.BigDecimal;
import java.util.List;

@SuppressWarnings("unused")
@DisplayName("Репозиторий комнат")
@DataJpaTest
@EnableJpaRepositories(basePackageClasses = RoomRepository.class)
@Transactional
public class JpaRoomRepositoryTest {

    @Autowired
    private RoomRepository jpaRoomRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("Положительный сценарий. Поиск комнаты по ее номеру.")
    @Test
    void findRoomByRoomNumber(){
        Integer expectedRoomNumber = 999;
        Room room = new Room(null, expectedRoomNumber, Room.Type.LUX, new BigDecimal("1000.0"), List.of());
        Room expectedRoom = testEntityManager.persist(room);
        Room actualRoom = jpaRoomRepository.findByRoomNumber(expectedRoomNumber).orElseThrow();
        System.out.println(jpaRoomRepository.findAll().size());
        Assertions.assertEquals(expectedRoom, actualRoom);
    }
}
