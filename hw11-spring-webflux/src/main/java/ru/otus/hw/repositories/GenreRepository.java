package ru.otus.hw.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.otus.hw.repositories.projections.GenreProjection;

public interface GenreRepository extends ReactiveCrudRepository<GenreProjection, Long> {
}
