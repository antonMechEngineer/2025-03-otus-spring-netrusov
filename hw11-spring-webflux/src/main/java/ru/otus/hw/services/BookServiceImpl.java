package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;
import ru.otus.hw.repositories.projections.AuthorProjection;
import ru.otus.hw.repositories.projections.BookProjection;
import ru.otus.hw.repositories.projections.GenreProjection;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorEntityRepository;
import ru.otus.hw.repositories.BookEntityRepository;
import ru.otus.hw.repositories.GenreEntityRepository;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorEntityRepository authorEntityRepository;

    private final GenreEntityRepository genreEntityRepository;

    private final BookEntityRepository bookEntityRepository;

    @Override
    public Mono<Book> findById(long id) {
        return bookEntityRepository.findById(id)
                .flatMap(this::enrichBookEntity)
                .map(BookProjection::toDomainObject);
    }

    @Override
    public Flux<Book> findAll() {
        return bookEntityRepository.findAll().map(BookProjection::toDomainObject);
    }

    @Override
    public Mono<Book> insert(Book book) {
        return bookEntityRepository.save(BookProjection.from(book))
                .flatMap(this::enrichBookEntity)
                .map(BookProjection::toDomainObject);
    }

    @Override
    public Mono<Book> update(Book book) {
        return bookEntityRepository.findById(book.getId())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Book is not found!")))
                .map(bookEntity -> book)
                .flatMap(this::enrichUpdatedBookEntity)
                .flatMap(bookEntityRepository::save)
                .map(BookProjection::toDomainObject);
    }

    @Override
    public Mono<Void> deleteById(long id) {
       return bookEntityRepository.deleteById(id);
    }

    private Mono<BookProjection> enrichBookEntity(BookProjection book) {
        return Mono.zip(
                Mono.just(book),
                authorEntityRepository.findById(book.getAuthorId()),
                genreEntityRepository.findById(book.getGenreId())
        ).map(this::mergeBookWithAuthorAndGenre);
    }

    private BookProjection mergeBookWithAuthorAndGenre(Tuple3<BookProjection, AuthorProjection, GenreProjection> tuple) {
        BookProjection b = tuple.getT1();
        AuthorProjection a = tuple.getT2();
        GenreProjection g = tuple.getT3();
        b.setAuthorFullName(a.getFullName());
        b.setGenreName(g.getName());
        return b;
    }

    private Mono<BookProjection> enrichUpdatedBookEntity(Book book) {
        return Mono.zip(
                Mono.just(BookProjection.from(book)),
                authorEntityRepository.findById(book.getAuthor().getId()),
                genreEntityRepository.findById(book.getGenre().getId())
        ).map(this::mergeBookWithAuthorAndGenre);
    }
}
