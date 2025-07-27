package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.security.SecurityConfiguration;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookPageController.class)
@Import({SecurityConfiguration.class})
public class BookPageControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private CommentService commentService;

    @Test
    void positiveBooks() throws Exception {
        mvc.perform(get("/books").with(user("abc").password("abc")))
                .andExpect(view().name("allBooks"));
    }

    @Test
    void positiveInsertBookForm() throws Exception {
        mvc.perform(get("/insertBook").param("id", "1").with(user("def").password("abc").roles("ADMIN")))
                .andExpect(view().name("insertBook"));
    }

    @Test
    void positiveEditBookForm() throws Exception {
        mvc.perform(get("/editBook").param("id", "1").with(user("abc").password("abc")))
                .andExpect(view().name("editBook"));
    }

    @Test
    void positiveBrowseBookDetails() throws Exception {
        mvc.perform(get("/browseBook").param("id", "1").with(user("abc").password("abc")))
                .andExpect(view().name("browseBook"));
    }

    @Test
    void positiveConfirmDeleteBook() throws Exception {
        mvc.perform(get("/deleteBook").param("id", "1").with(user("abc").password("abc")))
                .andExpect(view().name("deleteBook"));
    }
}