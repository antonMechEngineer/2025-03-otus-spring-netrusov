package ru.otus.hw.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.otus.hw.repositories.projections.BookProjection;

public interface BookRepository extends ReactiveCrudRepository<BookProjection, Long>, BookRepositoryCustom {
}
