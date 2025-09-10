package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.GenreService;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GenrePageController.class)
public class GenrePageControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private GenreService genreService;

    @Test
    void shouldReturnAllGenresPageWithGenresList() throws Exception {
        when(genreService.findAll()).thenReturn(Collections.emptyList());
        mvc.perform(get("/genres"))
                .andExpect(status().isOk())
                .andExpect(view().name("allGenres"));
    }

}
