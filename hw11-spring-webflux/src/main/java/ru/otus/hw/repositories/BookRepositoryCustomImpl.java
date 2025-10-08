package ru.otus.hw.repositories;

import io.r2dbc.spi.Readable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.repositories.projections.BookProjection;

@Repository
@RequiredArgsConstructor
public class BookRepositoryCustomImpl implements BookRepositoryCustom {

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

    private static final String SQL_SELECT_BOOK_BY_ID = """
            SELECT books.id AS book_id,
                   books.title AS book_title,
                   authors.id AS author_id,
                   authors.full_name AS author_full_name,
                   genres.id AS genre_id,
                   genres.name AS genre_name
            FROM books
            JOIN authors ON books.author_id = authors.id
            JOIN genres ON books.genre_id = genres.id
            WHERE books.id = :id;
            """;

    private final R2dbcEntityTemplate template;

    public Flux<BookProjection> findAll() {
        return template.getDatabaseClient().sql(SQL_ALL)
                .map(this::mapRecordToDto)
                .all();
    }

    @Override
    public Mono<BookProjection> findById(long id) {
        return template.getDatabaseClient().sql(SQL_SELECT_BOOK_BY_ID)
                .bind("id", id)
                .map(this::mapRecordToDto)
                .first();
    }

    private BookProjection mapRecordToDto(Readable record) {
        return new BookProjection(
                record.get("book_id", Long.class),
                record.get("book_title", String.class),
                record.get("author_id", Long.class),
                record.get("genre_id", Long.class),
                record.get("author_full_name", String.class),
                record.get("genre_name", String.class)
        );
    }
}
