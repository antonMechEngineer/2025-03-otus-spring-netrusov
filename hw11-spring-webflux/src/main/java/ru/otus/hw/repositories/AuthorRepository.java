package ru.otus.hw.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.otus.hw.repositories.projections.AuthorProjection;

public interface AuthorRepository extends ReactiveCrudRepository<AuthorProjection, Long> {

}
