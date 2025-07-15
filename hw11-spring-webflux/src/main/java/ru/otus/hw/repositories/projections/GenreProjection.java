package ru.otus.hw.repositories.projections;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import ru.otus.hw.models.Genre;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("genres")
public class GenreProjection {

    @Id
    private long id;

    private String name;

    public Genre toDomainObject() {
        return new Genre(id, name);
    }
}
