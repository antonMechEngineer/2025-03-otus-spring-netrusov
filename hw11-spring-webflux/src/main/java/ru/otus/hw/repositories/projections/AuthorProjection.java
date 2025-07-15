package ru.otus.hw.repositories.projections;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import ru.otus.hw.models.Author;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "authors")
public class AuthorProjection {

    @Id
    private long id;

    @Column("full_name")
    private String fullName;

    public Author toDomainObject() {
        return new Author(id, fullName);
    }
}
