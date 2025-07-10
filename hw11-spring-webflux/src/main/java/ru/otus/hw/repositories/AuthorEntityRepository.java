package ru.otus.hw.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.otus.hw.entities.AuthorEntity;

public interface AuthorEntityRepository extends ReactiveCrudRepository<AuthorEntity, Long> {

}
