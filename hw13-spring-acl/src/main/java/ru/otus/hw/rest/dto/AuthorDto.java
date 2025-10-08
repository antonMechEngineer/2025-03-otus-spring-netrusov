package ru.otus.hw.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.Author;

@Data
@AllArgsConstructor
public class AuthorDto {
    private long id;

    private String fullName;

    public static AuthorDto toDto(Author author) {
        return new AuthorDto(author.getId(), author.getFullName());
    }

    public Author toDomainObject() {
        return new Author(id, fullName);
    }
}
