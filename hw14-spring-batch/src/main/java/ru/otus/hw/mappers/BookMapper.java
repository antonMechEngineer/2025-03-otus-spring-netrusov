package ru.otus.hw.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Book;
import ru.otus.hw.projections.BookMongoProjection;

@RequiredArgsConstructor
@Component
public class BookMapper {

    private final AuthorMapper authorMapper;

    private final GenreMapper genreMapper;

    public BookMongoProjection toMongoProjection(Book book) {
        return new BookMongoProjection(
                String.valueOf(book.getId()),
                book.getTitle(),
                authorMapper.toMongoProjection(book.getAuthor()),
                genreMapper.toMongoProjection(book.getGenre()));
    }
}
