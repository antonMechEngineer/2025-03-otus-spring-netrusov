package ru.otus.hw.repositories.projections;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("comments")
public class CommentProjection {

    @Id
    private long id;

    @Column("payload_comment")
    private String payloadComment;

    @Column("book_id")
    private Long bookId;

    public Comment toDomainObject(Book book) {
        return new Comment(id, payloadComment, book);
    }
}
