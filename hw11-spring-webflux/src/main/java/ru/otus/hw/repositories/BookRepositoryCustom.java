package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.otus.hw.rest.dto.AuthorDto;
import ru.otus.hw.rest.dto.BookDto;
import io.r2dbc.spi.Readable;
import ru.otus.hw.rest.dto.GenreDto;

@Repository
@RequiredArgsConstructor
public class BookRepositoryCustom {

    private static final String SQL_ALL = """
            SELECT books.id AS book_id,
                                      books.title AS book_title,
                                      authors.id AS author_id,
                                      authors.full_name AS author_full_name,
                                      genres.id AS genre_id,
                                      genres.name AS genre_name
                               FROM books
                               JOIN authors ON books.author_id = authors.id
                               JOIN genres ON books.genre_id = genres.id;
            """;

    private final R2dbcEntityTemplate template;

    public Flux<BookDto> findAll() {
        return template.getDatabaseClient().inConnectionMany(connection ->
                Flux.from(connection.createStatement(SQL_ALL).execute())
                        .flatMap(result -> result.map(this::mapRecordToDto)));
    }

    private BookDto mapRecordToDto(Readable record) {
        return new BookDto(
                record.get("book_id", Long.class),
                record.get("book_title", String.class),
                new AuthorDto(record.get("author_id", Long.class), record.get("author_full_name", String.class)),
                new GenreDto(record.get("genre_id", Long.class), record.get("genre_name", String.class))
        );
    }
}
