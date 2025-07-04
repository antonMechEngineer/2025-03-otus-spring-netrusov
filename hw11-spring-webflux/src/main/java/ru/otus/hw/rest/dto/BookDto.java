package ru.otus.hw.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

@Data
@AllArgsConstructor
public class BookDto {

    private long id;

    private String title;

    private AuthorDto author;

    private GenreDto genre;

    public static BookDto toDto(Book book, Author author, Genre genre) {
        return new BookDto(
                book.getId(),
                book.getTitle(),
                new AuthorDto(author.getId(), author.getFullName()),
                new GenreDto(genre.getId(), genre.getName())
        );
    }

    public Book toDomainObject() {
        return new Book(id, title, author.getId(), genre.getId());
    }
}