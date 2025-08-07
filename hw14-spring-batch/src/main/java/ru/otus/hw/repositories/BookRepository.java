package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.projections.BookMongoProjection;

public interface BookRepository extends MongoRepository<BookMongoProjection, String> {
}
