package ru.otus.hw.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.otus.hw.entities.BookEntity;

public interface BookEntityRepository extends ReactiveCrudRepository<BookEntity, Long>, BookEntityRepositoryCustom {
}
