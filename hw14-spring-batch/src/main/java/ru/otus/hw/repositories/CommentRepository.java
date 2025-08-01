package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.projections.CommentMongoProjection;


public interface CommentRepository extends MongoRepository<CommentMongoProjection, String> {
}