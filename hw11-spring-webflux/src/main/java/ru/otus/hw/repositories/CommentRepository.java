package ru.otus.hw.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import ru.otus.hw.repositories.projections.CommentProjection;


public interface CommentRepository extends ReactiveCrudRepository<CommentProjection, Long> {

    Flux<CommentProjection> findByBookId(long bookId);
}
