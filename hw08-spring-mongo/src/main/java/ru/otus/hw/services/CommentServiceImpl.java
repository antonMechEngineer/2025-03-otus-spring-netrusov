package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Override
    public Optional<Comment> findById(String id) {
        return commentRepository.findById(id);
    }

    @Override
    public List<Comment> findByBook(String bookId) {
        return commentRepository.findByBookId(bookId);
    }

    @Override
    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }

    @Override
    public Comment insert(String payloadComment, String bookId) {
        return save(null, payloadComment, bookId);
    }

    @Override
    public Comment update(String id, String payloadComment, String bookId) {
        return save(id, payloadComment, bookId);
    }

    private Comment save(String id, String payloadComment, String bookId) {
        if (id != null) {
            commentRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Comment with id %s not found".formatted(id)));
        }
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookId)));
        var comment = new Comment(id, payloadComment, book);
        return commentRepository.save(comment);
    }
}
