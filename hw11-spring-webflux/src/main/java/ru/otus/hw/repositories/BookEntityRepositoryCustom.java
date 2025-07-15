package ru.otus.hw.repositories;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.repositories.projections.BookProjection;

public interface BookEntityRepositoryCustom {
    Flux<BookProjection> findAll();

    Mono<BookProjection> findById(long id);
}
