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
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "comments")
public class CommentMongoProjection {

    @Id
    private String id;

    private String payloadComment;

    @DocumentReference
    private BookMongoProjection bookMongoProjection;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CommentMongoProjection commentMongoProjection = (CommentMongoProjection) o;
        return Objects.equals(id, commentMongoProjection.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}