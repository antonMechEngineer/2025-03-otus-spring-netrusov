package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.rest.dto.BookDto;
import ru.otus.hw.rest.dto.CommentDto;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;

@RequiredArgsConstructor
@RestController
public class BookController {

    private final BookService bookService;

    private final CommentService commentService;

    @GetMapping("/api/books")
    public Flux<BookDto> findAllBooks() {
        return bookService.findAll().map(BookDto::toDto);
    }

    @GetMapping("/api/books/{id}")
    public Mono<ResponseEntity<BookDto>> findBookById(@PathVariable Long id) {
        return bookService.findById(id)
                .map(BookDto::toDto)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }

    @PostMapping("/api/books")
    public Mono<BookDto> createBook(@RequestBody BookDto bookDto) {
        return bookService.insert(bookDto.toDomainObject())
                .map(BookDto::toDto);
    }

    @PutMapping("/api/books/{id}")
    public Mono<BookDto> updateBook(@RequestBody BookDto updatedBookDto) {
        return bookService.update(updatedBookDto.toDomainObject())
                .map(BookDto::toDto);
    }

    @DeleteMapping("/api/books/{id}")
    public Mono<ResponseEntity<Void>> deleteBook(@PathVariable Long id) {
        return bookService.deleteById(id).map(ResponseEntity::ok);
    }

    @GetMapping("/api/books/{id}/comments")
    public Flux<CommentDto> getBookComments(@PathVariable Long id) {
        return commentService.findCommentsByBook(bookService.findById(id)).map(CommentDto::toDto);
    }
}
