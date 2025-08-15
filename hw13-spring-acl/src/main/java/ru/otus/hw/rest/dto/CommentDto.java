package ru.otus.hw.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.Comment;

@Data
@AllArgsConstructor
public class CommentDto {

    private long id;

    private String payloadComment;

    private long bookId;

    public static CommentDto toDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getPayloadComment(),
                comment.getBook().getId() // Получаем только ID книги
        );
    }

    public Comment toDomainObject() {
        return new Comment(id, payloadComment, null);
    }
}