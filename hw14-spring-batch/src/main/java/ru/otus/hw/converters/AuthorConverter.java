package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.Author;
import ru.otus.hw.projections.AuthorMongoProjection;

@Component
public class AuthorConverter {

    public AuthorMongoProjection toMongoProjection(Author author) {
        return new AuthorMongoProjection(String.valueOf(author.getId()), author.getFullName());
    }
}
