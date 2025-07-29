package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.Genre;
import ru.otus.hw.projections.GenreMongoProjection;

@Component
public class GenreConverter {

    public GenreMongoProjection toMongoProjection(Genre genre) {
        return new GenreMongoProjection(String.valueOf(genre.getId()), genre.getName());
    }
}
