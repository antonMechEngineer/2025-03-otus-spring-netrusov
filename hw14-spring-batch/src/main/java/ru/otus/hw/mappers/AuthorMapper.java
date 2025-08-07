package ru.otus.hw.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.Author;
import ru.otus.hw.projections.AuthorMongoProjection;

@Component
public class AuthorMapper {

    public AuthorMongoProjection toMongoProjection(Author author) {
        return new AuthorMongoProjection(String.valueOf(author.getId()), author.getFullName());
    }
}
