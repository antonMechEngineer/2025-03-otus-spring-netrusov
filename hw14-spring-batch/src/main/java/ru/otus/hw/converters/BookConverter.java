package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Book;
import ru.otus.hw.projections.BookMongoProjection;

@RequiredArgsConstructor
@Component
public class BookConverter {

    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    public BookMongoProjection toMongoProjection(Book book) {
        return new BookMongoProjection(
                String.valueOf(book.getId()),
                book.getTitle(),
                authorConverter.toMongoProjection(book.getAuthor()),
                genreConverter.toMongoProjection(book.getGenre()));
    }
}
