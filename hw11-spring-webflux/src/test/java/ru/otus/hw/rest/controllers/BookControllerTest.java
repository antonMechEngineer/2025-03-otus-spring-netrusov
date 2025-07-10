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
import ru.otus.hw.rest.BookController;
import ru.otus.hw.rest.dto.AuthorDto;
import ru.otus.hw.rest.dto.BookDto;
import ru.otus.hw.rest.dto.CommentDto;
import ru.otus.hw.rest.dto.GenreDto;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@WebFluxTest(controllers = BookController.class)
class BookControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private BookService bookService;

    @MockBean
    private CommentService commentService;

    private List<Book> mockBooks;
    private List<Author> mockAuthors;
    private List<Genre> mockGenres;

    @BeforeEach
    void setup() {
        mockBooks = List.of(
                new Book(1L, "b1", new Author(1L, "a1"), new Genre(1L , "g1")),
                new Book(2L, "b2", new Author(2L, "a2"), new Genre(2L , "g2"))
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
        given(bookService.findAll()).willReturn(Flux.fromIterable(mockBooks));

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
        given(bookService.findAll()).willReturn(Flux.empty());

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
        given(bookService.findById(eq(book.getId()))).willReturn(Mono.just(book));

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
        given(bookService.findById(eq(123L))).willReturn(Mono.empty()); // несуществующий ID

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
        var savedBook = new Book(3L, "b3", new Author(1L, "a1"), new Genre(1L , "g1"));

        given(bookService.insert(any())).willReturn(Mono.just(savedBook));

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
        var updatedBook = new Book(existingBook.getId(), "b4", new Author(2L, "a2"), new Genre(2L , "g2"));
        given(bookService.update(any())).willReturn(Mono.just(updatedBook));

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
        given(bookService.deleteById(eq(book.getId()))).willReturn(Mono.empty());
        webTestClient.delete()
                .uri("/api/books/" + book.getId())
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldFetchCommentsOfSpecificBook() {
        var book = mockBooks.get(0);
        given(bookService.findById(eq(book.getId()))).willReturn(Mono.just(book));
        List<Comment> comments = List.of(new Comment(1L, "c", book));
        given(commentService.findCommentsByBook(any()))
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