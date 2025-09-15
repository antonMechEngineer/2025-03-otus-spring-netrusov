package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @CircuitBreaker(name = "dbBreaker")
    public Optional<Comment> findById(long id) {
        return commentRepository.findById(id);
    }


    @Override
    @CircuitBreaker(name = "dbBreaker")
    public List<Comment> findByBook(long bookId) {
        return commentRepository.findByBookId(bookId);
    }

    @Transactional
    @Override
    @CircuitBreaker(name = "dbBreaker")
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

    @Transactional
    @Override
    @CircuitBreaker(name = "dbBreaker")
    public Comment insert(String payloadComment, long bookId) {
        return save(0, payloadComment, bookId);
    }

    @Transactional
    @Override
    @CircuitBreaker(name = "dbBreaker")
    public Comment update(long id, String payloadComment, long bookId) {
        return save(id, payloadComment, bookId);
    }

    private Comment save(long id, String payloadComment, long bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));
        var comment = new Comment(id, payloadComment, book);
        return commentRepository.save(comment);
    }
}
