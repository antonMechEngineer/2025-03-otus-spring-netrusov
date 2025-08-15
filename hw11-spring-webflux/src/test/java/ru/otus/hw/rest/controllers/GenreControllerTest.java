package ru.otus.hw.rest.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import ru.otus.hw.models.Genre;
import ru.otus.hw.rest.GenreController;
import ru.otus.hw.rest.dto.GenreDto;
import ru.otus.hw.services.GenreService;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.mockito.BDDMockito.given;

@WebFluxTest(controllers = GenreController.class)
class GenreControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GenreService genreService;

    @Test
    void testGetAllGenres_Success() {
        List<Genre> mockGenres = List.of(
                new Genre(1L, "a"),
                new Genre(2L, "b"));

        given(genreService.findAll()).willReturn(Flux.fromIterable(mockGenres));
        webTestClient.get()
                .uri("/api/genres")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(GenreDto.class)
                .value(genres -> assertThat(genres.size()).isEqualTo(2))
                .value(genres -> assertThat(genres.stream().anyMatch(g -> g.getName().equals("a"))).isTrue())
                .value(genres -> assertThat(genres.stream().anyMatch(g -> g.getName().equals("b"))).isTrue());
    }

    @Test
    void testGetAllGenres_Empty() {
        given(genreService.findAll()).willReturn(Flux.empty());
        webTestClient.get()
                .uri("/api/genres")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(GenreDto.class)
                .isEqualTo(List.of());
    }
}