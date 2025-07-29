package ru.otus.hw.projections;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "books")
public class  BookMongoProjection {

    @Id
    private String id;

    private String title;

    @DocumentReference
    private AuthorMongoProjection authorMongoProjection;

    @DocumentReference
    private GenreMongoProjection genreMongoProjection;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BookMongoProjection bookMongoProjection = (BookMongoProjection) o;
        return Objects.equals(id, bookMongoProjection.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
