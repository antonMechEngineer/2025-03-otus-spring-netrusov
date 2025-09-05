//package ru.otus.hw.controllers;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(BookPageController.class)
//
//public class BookPageControllerTest {
//
//    @Autowired
//    private MockMvc mvc;
//
//    @MockBean
//    private BookService bookService;
//
//    @MockBean
//    private AuthorService authorService;
//
//    @MockBean
//    private GenreService genreService;
//
//    @MockBean
//    private CommentService commentService;
//
//    private final Author author = new Author(1L, "Author Name");
//    private final Genre genre = new Genre(1L, "Genre Name");
//    private final Book book = new Book(1L, "Title", author, genre);
//
//    @Test
//    void shouldReturnAllBooksPage() throws Exception {
//        List<Book> books = List.of(book);
//        when(bookService.findAll()).thenReturn(books);
//
//        mvc.perform(get("/books"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("allBooks"));
//    }
//
//    @Test
//    void shouldReturnEditBookPageWithModel() throws Exception {
//        when(bookService.findById(1L)).thenReturn(Optional.of(book));
//        when(authorService.findAll()).thenReturn(List.of(author));
//        when(genreService.findAll()).thenReturn(List.of(genre));
//        mvc.perform(get("/editBook").param("id", "1"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("editBook"));
//    }
//
//    @Test
//    void shouldReturnInsertBookPageWithModel() throws Exception {
//        when(authorService.findAll()).thenReturn(List.of(author));
//        when(genreService.findAll()).thenReturn(List.of(genre));
//        mvc.perform(get("/insertBook"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("insertBook"));
//    }
//
//    @Test
//    void shouldReturnBrowseBookPageWithComments() throws Exception {
//        List<Comment> comments = List.of(new Comment(1L, "text", book));
//        when(bookService.findById(1L)).thenReturn(Optional.of(book));
//        when(commentService.findByBook(1L)).thenReturn(comments);
//        mvc.perform(get("/browseBook").param("id", "1"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("browseBook"));
//    }
//}
