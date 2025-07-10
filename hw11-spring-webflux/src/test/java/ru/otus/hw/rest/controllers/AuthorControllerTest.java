package ru.otus.hw.rest.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorEntityRepository;
import ru.otus.hw.rest.AuthorController;
import ru.otus.hw.rest.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

@WebFluxTest(controllers = AuthorController.class)
public class AuthorControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AuthorService authorService;

    @Test
    void testGetAllAuthors_Success() {
        List<Author> mockAuthors = List.of(
                new Author(1L, "a"),
                new Author(2L, "b"));

        given(authorService.findAll()).willReturn(Flux.fromIterable(mockAuthors));
        webTestClient.get()
                .uri("/api/authors")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(AuthorDto.class)
                .value(authors -> assertThat(authors.size()).isEqualTo(2))
                .value(authors -> assertThat(authors.stream().anyMatch(a -> a.getFullName().equals("a"))).isTrue())
                .value(authors -> assertThat(authors.stream().anyMatch(a -> a.getFullName().equals("b"))).isTrue());
    }

    @Test
    void testGetAllAuthors_Empty() {
        given(authorService.findAll()).willReturn(Flux.empty());
        webTestClient.get()
                .uri("/api/authors")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(AuthorDto.class)
                .isEqualTo(List.of());
    }
}



