package ru.otus.hw.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.Book;

@Data
@AllArgsConstructor
public class BookDto {

    private long id;

    private String title;

    private long authorId;

    private long genreId;

    private String authorFullName;

    private String genreName;

    public static BookDto toDto(Book book) {
        return new BookDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor().getId(),
                book.getGenre().getId(),
                book.getAuthor().getFullName(),
                book.getGenre().getName()
        );
    }

    public Book toDomainObject() {
        return new Book(id, title, null, null);
    }
}