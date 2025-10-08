package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

public interface CommentService {
    Flux<Comment> findCommentsByBook(Mono<Book> book);
}
