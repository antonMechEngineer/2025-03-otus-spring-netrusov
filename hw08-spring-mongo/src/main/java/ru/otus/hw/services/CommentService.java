package ru.otus.hw.services;

import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    Optional<Comment> findById(long id);

    List<Comment> findByBook(long bookId);

    Comment insert(String payloadComment, long bookId);

    Comment update(long id, String payloadComment, long bookId);

    void deleteById(long id);

}
