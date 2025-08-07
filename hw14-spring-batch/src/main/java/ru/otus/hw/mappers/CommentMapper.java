package ru.otus.hw.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Comment;
import ru.otus.hw.projections.CommentMongoProjection;

@RequiredArgsConstructor
@Component
public class CommentMapper {

    private final BookMapper bookMapper;

    public CommentMongoProjection toMongoProjection(Comment comment) {
        return new CommentMongoProjection(
                String.valueOf(comment.getId()),
                comment.getPayloadComment(),
                bookMapper.toMongoProjection(comment.getBook()));
    }
}
