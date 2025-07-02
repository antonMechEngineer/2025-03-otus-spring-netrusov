package ru.otus.hw.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.rest.BookController;
import ru.otus.hw.rest.GlobalExceptionHandler;
import ru.otus.hw.rest.dto.BookDto;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = BookController.class)
@WebMvcTest(controllers = BookController.class)
@Import(GlobalExceptionHandler.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testFindAllBooks() throws Exception {
        List<Book> expectedBooks = List.of(
                new Book(1, "Book 1", new Author(1L, "Author 1"), new Genre(1L, "Genre")),
                new Book(2, "Book 2", new Author(1L, "Author 1"), new Genre(1L, "Genre"))
        );
        given(bookService.findAll()).willReturn(List.copyOf(expectedBooks));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].title").value("Book 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].authorFullName").value("Author 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].genreName").value("Genre"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].id").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].title").value("Book 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].authorFullName").value("Author 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].genreName").value("Genre"));
    }

    @Test
    void testFindBookById() throws Exception {
        Book expectedBook = new Book(1, "Book 1", new Author(1L, "Author 1"), new Genre(1L, "Genre"));
        given(bookService.findById(anyLong())).willReturn(Optional.of(expectedBook));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Book 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorFullName").value("Author 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genreName").value("Genre"));
    }

    @Test
    void testCreateBook() throws Exception {
        BookDto requestBook = new BookDto(0, "New Book", 1L, 1L, "", "");
        Book mockBook = new Book(1, "New Book", new Author(1L, "Author New"), new Genre(1L, "Genre New"));
        given(bookService.insert(requestBook.getTitle(), requestBook.getAuthorId(), requestBook.getGenreId()))
                .willReturn(mockBook);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/books")
                        .content(objectMapper.writeValueAsBytes(requestBook))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("New Book"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorFullName").value("Author New"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genreName").value("Genre New"));
    }

    @Test
    void testUpdateBook() throws Exception {
        BookDto updatedBook = new BookDto(1L, "Updated Book", 1L, 1L, "Author Updated", "Genre Updated");
        Book mockBook = new Book(1, "Updated Book", new Author(1L, "Author Updated"), new Genre(1L, "Genre Updated"));
        given(bookService.findById(1L)).willReturn(Optional.of(mockBook));
        given(bookService.update(updatedBook.getId(), updatedBook.getTitle(), updatedBook.getAuthorId(), updatedBook.getGenreId()))
                .willReturn(mockBook);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/books/1")
                        .content(objectMapper.writeValueAsBytes(updatedBook))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Updated Book"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorFullName").value("Author Updated"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genreName").value("Genre Updated"));
    }

    @Test
    void testDeleteBook() throws Exception {
        Book book = new Book(1L, "abc", new Author(1L, "a"), new Genre(1L, "a"));
        given(bookService.findById(1L)).willReturn(Optional.of(book));
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/books/1"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetBookComments() throws Exception {
        Book mockBook = new Book(1, "Book", new Author(1L, "Author"), new Genre(1L, "Genre"));
        List<Comment> expectedComments = List.of(new Comment(1L, "First comment", mockBook), new Comment(2L, "Second comment", mockBook));
        given(commentService.findByBook(1L)).willReturn(List.copyOf(expectedComments));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/1/comments"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].payloadComment").value("First comment"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].id").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].payloadComment").value("Second comment"));
    }
}