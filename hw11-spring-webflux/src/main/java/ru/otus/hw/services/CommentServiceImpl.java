package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.CommentRepository;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    public Flux<Comment> findCommentsByBook(Mono<Book> bookMono) {
        return bookMono.flatMapMany(book ->
                commentRepository.findByBookId(book.getId())
                        .map(comment -> comment.toDomainObject(book))
        );
    }
}
