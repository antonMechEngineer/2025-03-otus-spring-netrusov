package ru.otus.hw.repositories;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Repository
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public JdbcBookRepository(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public Optional<Book> findById(long id) {
        String query = """
                SELECT b.id, b.title, b.author_id, b.genre_id,a.full_name AS author_name, g.name AS genre_name
                FROM books b
                JOIN authors a ON b.author_id = a.id
                JOIN genres g ON b.genre_id = g.id
                WHERE b.id = :id
                """;
        List<Book> books = namedParameterJdbcOperations.query(query, Map.of("id", id), new BookRowMapper());
        if (books.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(books.get(0));
        }
    }

    @Override
    public List<Book> findAll() {
        String query = """
                SELECT b.id, b.title, b.author_id, b.genre_id, a.full_name AS author_name, g.name AS genre_name
                FROM books b
                JOIN authors a ON b.author_id = a.id
                JOIN genres g ON b.genre_id = g.id
                """;
        return namedParameterJdbcOperations.query(query, new BookRowMapper());
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        namedParameterJdbcOperations.update("DELETE FROM books WHERE id = :id", Map.of("id", id));
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("title", book.getTitle());
        mapSqlParameterSource.addValue("author", book.getAuthor().getId());
        mapSqlParameterSource.addValue("genre", book.getGenre().getId());
        namedParameterJdbcOperations.update(
                "INSERT INTO books (title, author_id, genre_id) VALUES (:title, :author, :genre)",
                mapSqlParameterSource,
                keyHolder,
                new String[]{"id"});
        book.setId(keyHolder.getKeyAs(Long.class));
        return book;
    }

    private Book update(Book book) {
        int updatedRow = namedParameterJdbcOperations.update(
                "UPDATE books SET title = :title, author_id = :author, genre_id = :genre WHERE id = :id",
                Map.of("title", book.getTitle(),
                        "author", book.getAuthor().getId(),
                        "genre", book.getGenre().getId(),
                        "id", book.getId()));
        if (updatedRow > 0) {
            return book;
        } else {
            throw new EntityNotFoundException("Book not found!");
        }
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String title = rs.getString("title");
            Author author = new Author(rs.getLong("author_id"), rs.getString("author_name"));
            Genre genre = new Genre(rs.getLong("genre_id"), rs.getString("genre_name"));
            return new Book(id, title, author, genre);
        }
    }
}
