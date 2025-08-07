package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.projections.AuthorMongoProjection;

public interface AuthorRepository extends MongoRepository<AuthorMongoProjection, String> {

}
