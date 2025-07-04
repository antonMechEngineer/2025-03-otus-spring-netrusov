package ru.otus.hw.rest.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.BookRepositoryCustom;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.rest.BookController;
import ru.otus.hw.rest.dto.BookDto;
import ru.otus.hw.rest.dto.AuthorDto;
import ru.otus.hw.rest.dto.CommentDto;
import ru.otus.hw.rest.dto.GenreDto;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@WebFluxTest(controllers = BookController.class)
class BookControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private BookRepositoryCustom bookRepositoryCustom;

    private List<Book> mockBooks;
    private List<Author> mockAuthors;
    private List<Genre> mockGenres;

    @BeforeEach
    void setup() {
        mockBooks = List.of(
                new Book(1L, "b1", 1L, 1L),
                new Book(2L, "b2", 2L, 2L)
        );

        mockAuthors = List.of(
                new Author(1L, "a1"),
                new Author(2L, "a2")
        );

        mockGenres = List.of(
                new Genre(1L, "g1"),
                new Genre(2L, "g2")
        );
    }

    private AuthorDto convertToAuthorDto(Author author) {
        return new AuthorDto(author.getId(), author.getFullName());
    }

    private GenreDto convertToGenreDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }


    @Test
    void shouldFindAllBooksSuccessfully() {
        Author author = new Author(1L, "a1");
        Genre genre = new Genre(1L, "g1");
        List<BookDto> bookDtoList = mockBooks.stream().map(book-> BookDto.toDto(book, author, genre)).toList();
        given(bookRepositoryCustom.findAll()).willReturn(Flux.fromIterable(bookDtoList));
        webTestClient.get()
                .uri("/api/books")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(BookDto.class)
                .value(books -> assertThat(books.size()).isEqualTo(2))
                .value(books -> assertThat(books.stream().anyMatch(b -> b.getTitle().equals("b1"))).isTrue())
                .value(books -> assertThat(books.stream().anyMatch(b -> b.getTitle().equals("b2"))).isTrue());
    }

    @Test
    void shouldHandleNoBooksCase() {
        given(bookRepositoryCustom.findAll()).willReturn(Flux.empty());

        webTestClient.get()
                .uri("/api/books")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(BookDto.class)
                .isEqualTo(List.of());
    }

    @Test
    void shouldFindBookByIdSuccessfully() {
        var book = mockBooks.get(0);
        given(bookRepository.findById(eq(book.getId()))).willReturn(Mono.just(book));
        given(authorRepository.findById(eq(book.getAuthorId()))).willReturn(Mono.just(mockAuthors.get(0)));
        given(genreRepository.findById(eq(book.getGenreId()))).willReturn(Mono.just(mockGenres.get(0)));

        webTestClient.get()
                .uri("/api/books/" + book.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(BookDto.class)
                .value(bookDto -> assertThat(bookDto.getTitle()).isEqualTo("b1"))
                .value(bookDto -> assertThat(bookDto.getAuthor().getFullName()).isEqualTo("a1"))
                .value(bookDto -> assertThat(bookDto.getGenre().getName()).isEqualTo("g1"));
    }

    @Test
    void shouldHandleBookNotFound() {
        given(bookRepository.findById(eq(123L))).willReturn(Mono.empty()); // несуществующий ID

        webTestClient.get()
                .uri("/api/books/123")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void shouldCreateNewBookSuccessfully() {
        var newBookDto = new BookDto(-1L, "b3",
                convertToAuthorDto(mockAuthors.get(0)),
                convertToGenreDto(mockGenres.get(0)));
        var savedBook = new Book(3L, "b3", 1L, 1L);

        given(bookRepository.save(any())).willReturn(Mono.just(savedBook));
        given(authorRepository.findById(eq(1L))).willReturn(Mono.just(mockAuthors.get(0)));
        given(genreRepository.findById(eq(1L))).willReturn(Mono.just(mockGenres.get(0)));

        webTestClient.post()
                .uri("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newBookDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookDto.class)
                .value(bookDto -> assertThat(bookDto.getTitle()).isEqualTo("b3"))
                .value(bookDto -> assertThat(bookDto.getAuthor().getFullName()).isEqualTo("a1"))
                .value(bookDto -> assertThat(bookDto.getGenre().getName()).isEqualTo("g1"));
    }


    @Test
    void shouldUpdateExistingBookSuccessfully() {
        var existingBook = mockBooks.get(0);
        var updatedBookDto = new BookDto(existingBook.getId(), "b4",
                convertToAuthorDto(mockAuthors.get(1)),
                convertToGenreDto(mockGenres.get(1)));
        var updatedBook = new Book(existingBook.getId(), "b4", 2L, 2L);

        given(bookRepository.findById(eq(existingBook.getId()))).willReturn(Mono.just(existingBook));
        given(bookRepository.save(any())).willReturn(Mono.just(updatedBook));
        given(authorRepository.findById(eq(2L))).willReturn(Mono.just(mockAuthors.get(1)));
        given(genreRepository.findById(eq(2L))).willReturn(Mono.just(mockGenres.get(1)));

        webTestClient.put()
                .uri("/api/books/" + existingBook.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedBookDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookDto.class)
                .value(bookDto -> assertThat(bookDto.getTitle()).isEqualTo("b4"))
                .value(bookDto -> assertThat(bookDto.getAuthor().getFullName()).isEqualTo("a2"))
                .value(bookDto -> assertThat(bookDto.getGenre().getName()).isEqualTo("g2"));
    }


    @Test
    void shouldDeleteBookSuccessfully() {
        var book = mockBooks.get(0);
        given(bookRepository.deleteById(eq(book.getId()))).willReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/books/" + book.getId())
                .exchange()
                .expectStatus().isOk();
    }


    @Test
    void shouldFetchCommentsOfSpecificBook() {
        List<Comment> comments = List.of(new Comment(1L, "c", 1L));
        given(commentRepository.findByBookId(longThat(l -> l == 1L)))
                .willReturn(Flux.fromIterable(comments));
        webTestClient.get()
                .uri("/api/books/1/comments")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(CommentDto.class)
                .isEqualTo(comments.stream().map(CommentDto::toDto).toList());
    }
}