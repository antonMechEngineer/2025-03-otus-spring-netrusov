package ru.otus.hw.controllers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.models.Author;
import ru.otus.hw.services.AuthorService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthorService authorService;

    @Test
    void shouldReturnAllAuthorsPageWithAuthorsList() throws Exception {
        List<Author> authors = List.of(
                new Author(1L, "a"),
                new Author(2L, "b")
        );

        when(authorService.findAll()).thenReturn(authors);

        mvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(view().name("allAuthors"))
                .andExpect(model().attribute("authors", authors));
    }
}
