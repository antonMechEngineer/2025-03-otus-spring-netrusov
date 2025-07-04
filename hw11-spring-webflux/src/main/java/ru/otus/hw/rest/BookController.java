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
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.repositories.BookRepositoryCustom;
import ru.otus.hw.rest.dto.BookDto;
import ru.otus.hw.rest.dto.CommentDto;

@RequiredArgsConstructor
@RestController
public class BookController {

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final CommentRepository commentRepository;

    private final BookRepositoryCustom bookRepositoryCustom;

    @GetMapping("/api/books")
    public Flux<BookDto> findAllBooks() {
        return bookRepositoryCustom.findAll();
    }

    @GetMapping("/api/books/{id}")
    public Mono<ResponseEntity<BookDto>> findBookById(@PathVariable Long id) {
        return bookRepository.findById(id)
                .flatMap(book ->
                        Mono.zip(
                                Mono.just(book),
                                authorRepository.findById(book.getAuthorId()),
                                genreRepository.findById(book.getGenreId()))
                )
                .map(tuple -> {
                    Book b = tuple.getT1();
                    Author a = tuple.getT2();
                    Genre g = tuple.getT3();
                    return BookDto.toDto(b, a, g);
                })
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }

    @PostMapping("/api/books")
    public Mono<BookDto> createBook(@RequestBody BookDto bookDto) {
        return bookRepository.save(bookDto.toDomainObject())
                .flatMap(book ->
                        Mono.zip(
                                Mono.just(book),
                                authorRepository.findById(book.getAuthorId()),
                                genreRepository.findById(book.getGenreId()))
                )
                .map(tuple -> {
                    Book b = tuple.getT1();
                    Author a = tuple.getT2();
                    Genre g = tuple.getT3();
                    return BookDto.toDto(b, a, g);
                });
    }

    @PutMapping("/api/books/{id}")
    public Mono<BookDto> updateBook(@PathVariable Long id, @RequestBody BookDto updatedBookDto) {
        return bookRepository.findById(id)
                .flatMap(book ->
                        Mono.zip(
                                bookRepository.save(updatedBookDto.toDomainObject()),
                                authorRepository.findById(updatedBookDto.getAuthor().getId()),
                                genreRepository.findById(updatedBookDto.getGenre().getId()))
                )
                .map(tuple -> {
                    Book b = tuple.getT1();
                    Author a = tuple.getT2();
                    Genre g = tuple.getT3();
                    return BookDto.toDto(b, a, g);
                });
    }

    @DeleteMapping("/api/books/{id}")
    public Mono<ResponseEntity<Void>> deleteBook(@PathVariable Long id) {
        return bookRepository.deleteById(id).map(ResponseEntity::ok);
    }

    @GetMapping("/api/books/{id}/comments")
    public Flux<CommentDto> getBookComments(@PathVariable Long id) {
        return commentRepository.findByBookId(id).map(CommentDto::toDto);
    }
}
