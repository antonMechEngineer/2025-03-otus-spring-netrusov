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
    public Optional<Comment> findById(long id) {
        return commentRepository.findById(String.valueOf(id));
    }


    @Override
    public List<Comment> findByBook(long bookId) {
        return commentRepository.findByBookId(String.valueOf(bookId));
    }

    @Override
    public void deleteById(long id) {
        commentRepository.deleteById(String.valueOf(id));
    }

    @Override
    public Comment insert(String payloadComment, long bookId) {
        return save(0, payloadComment, bookId);
    }

    @Override
    public Comment update(long id, String payloadComment, long bookId) {
        return save(id, payloadComment, bookId);
    }

    private Comment save(long id, String payloadComment, long bookId) {
        if (id > 0) {
            commentRepository.findById(String.valueOf(id))
                    .orElseThrow(() -> new EntityNotFoundException("Comment with id %d not found".formatted(id)));
        }
        var book = bookRepository.findById(String.valueOf(bookId))
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));
        var comment = new Comment(String.valueOf(id), payloadComment, book);
        return commentRepository.save(comment);
    }
}
