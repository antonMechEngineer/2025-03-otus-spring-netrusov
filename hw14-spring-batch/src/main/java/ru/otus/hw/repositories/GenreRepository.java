package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.projections.GenreMongoProjection;

public interface GenreRepository extends MongoRepository<GenreMongoProjection, String> {
}
