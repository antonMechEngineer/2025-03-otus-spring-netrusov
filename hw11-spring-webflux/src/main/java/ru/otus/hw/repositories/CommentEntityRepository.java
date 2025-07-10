package ru.otus.hw.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import ru.otus.hw.entities.CommentEntity;


public interface CommentEntityRepository extends ReactiveCrudRepository<CommentEntity, Long> {

    Flux<CommentEntity> findByBookId(long bookId);
}
