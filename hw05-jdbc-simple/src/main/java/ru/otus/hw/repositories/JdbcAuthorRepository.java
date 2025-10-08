package ru.otus.hw.repositories;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcAuthorRepository implements AuthorRepository {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public JdbcAuthorRepository(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public List<Author> findAll() {
        String query = "SELECT id, full_name FROM authors";
        return namedParameterJdbcOperations.query(query, new AuthorRowMapper());
    }

    @Override
    public Optional<Author> findById(long id) {
        String query = "SELECT id, full_name FROM authors WHERE id = :id";
        List<Author> authors = namedParameterJdbcOperations.query(query, Map.of("id", id), new AuthorRowMapper());
        if (authors.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(authors.get(0));
        }
    }

    private static class AuthorRowMapper implements RowMapper<Author> {
        @Override
        public Author mapRow(ResultSet rs, int i) throws SQLException {
            return new Author(rs.getLong("id"), rs.getString("full_name"));
        }
    }
}
