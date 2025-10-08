package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.GenreService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GenreController.class)
public class GenreControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private GenreService genreService;

    @Test
    void shouldReturnAllGenresPageWithGenresList() throws Exception {
        List<Genre> genres = List.of(
                new Genre(1L, "a"),
                new Genre(2L, "b")
        );

        when(genreService.findAll()).thenReturn(genres);

        mvc.perform(get("/genres"))
                .andExpect(status().isOk())
                .andExpect(view().name("allGenres"))
                .andExpect(model().attribute("genres", genres));
    }

}
