package ru.otus.hw.repositories.projections;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "books")
public class BookProjection {

    @Id
    private long id;

    @Column("title")
    private String title;

    @Column("author_id")
    private Long authorId;

    @Column("genre_id")
    private Long genreId;

    @Transient
    private String authorFullName;

    @Transient
    private String genreName;

    public Book toDomainObject() {
        return new Book(id, title, new Author(authorId, authorFullName), new Genre(genreId, genreName));
    }

    public static BookProjection from(Book book) {
        return new BookProjection(book.getId(),
                book.getTitle(),
                book.getAuthor().getId(),
                book.getGenre().getId(),
                book.getAuthor().getFullName(),
                book.getGenre().getName());
    }

}
