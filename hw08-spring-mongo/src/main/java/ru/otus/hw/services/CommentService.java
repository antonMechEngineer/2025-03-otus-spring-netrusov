package ru.otus.hw.services;

import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    Optional<Comment> findById(String id);

    List<Comment> findByBook(String bookId);

    Comment insert(String payloadComment, String bookId);

    Comment update(String id, String payloadComment, String bookId);

    void deleteById(String id);

}
