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
                .andExpect(status().isOk())
                .andExpect(view().name("allBooks"));
    }

    @Test
    void negativeBooks() throws Exception {
        mvc.perform(get("/books"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void positiveInsertBookForm() throws Exception {
        mvc.perform(get("/insertBook").param("id", "1").with(user("abc").password("abc")))
                .andExpect(status().isOk())
                .andExpect(view().name("insertBook"));
    }

    @Test
    void negativeInsertBookForm() throws Exception {
        mvc.perform(get("/insertBook").param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void positiveEditBookForm() throws Exception {
        mvc.perform(get("/editBook").param("id", "1").with(user("abc").password("abc")))
                .andExpect(status().isOk())
                .andExpect(view().name("editBook"));
    }

    @Test
    void negativeEditBookForm() throws Exception {
        mvc.perform(get("/editBook").param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void positiveBrowseBookDetails() throws Exception {
        mvc.perform(get("/browseBook").param("id", "1").with(user("abc").password("abc")))
                .andExpect(status().isOk())
                .andExpect(view().name("browseBook"));
    }

    @Test
    void negativeBrowseBookDetails() throws Exception {
        mvc.perform(get("/browseBook").param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void positiveConfirmDeleteBook() throws Exception {
        mvc.perform(get("/deleteBook").param("id", "1").with(user("abc").password("abc")))
                .andExpect(status().isOk())
                .andExpect(view().name("deleteBook"));
    }

    @Test
    void negativeConfirmDeleteBook() throws Exception {
        mvc.perform(get("/deleteBook").param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }
}