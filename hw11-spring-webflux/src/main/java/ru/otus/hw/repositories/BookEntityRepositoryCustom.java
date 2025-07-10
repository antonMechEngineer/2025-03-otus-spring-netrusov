package ru.otus.hw.repositories;

import reactor.core.publisher.Flux;
import ru.otus.hw.entities.BookEntity;

public interface BookEntityRepositoryCustom {
    Flux<BookEntity> findAll();
}
