package ru.otus.hw.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.otus.hw.entities.GenreEntity;

public interface GenreEntityRepository extends ReactiveCrudRepository<GenreEntity, Long> {
}
