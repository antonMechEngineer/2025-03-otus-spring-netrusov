package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final CommentService commentService;

    private final AuthorService authorService;

    private final GenreService genreService;

    @GetMapping("/books")
    public String findAllBooks(Model model) {
        List<Book> books = bookService.findAll();
        model.addAttribute("books", books);
        return "allBooks";
    }

    @GetMapping("/editBook")
    public String editBookPage(@RequestParam("id") long id, Model model) {
        Book book = bookService.findById(id).orElseThrow(NotFoundException::new);
        model.addAttribute("book", book);
        List<Author> authors = authorService.findAll();
        List<Genre> genres = genreService.findAll();
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        return "editBook";
    }

    @PostMapping("/editBook")
    public String updateBook(Book book) {
        bookService.update(book.getId(), book.getTitle(), book.getAuthor().getId(), book.getGenre().getId());
        return "redirect:/books";
    }

    @GetMapping("/insertBook")
    public String insertBookPage(Model model) {
        model.addAttribute("book", new Book());
        List<Author> authors = authorService.findAll();
        List<Genre> genres = genreService.findAll();
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        return "insertBook";
    }

    @PostMapping("/insertBook")
    public String insertBook(@ModelAttribute Book book) {
        bookService.insert(book.getTitle(), book.getAuthor().getId(), book.getGenre().getId());
        return "redirect:/books";
    }

    @GetMapping("/deleteBook")
    public String deleteBookPage(@RequestParam("id") long id, Model model) {
        Book book = bookService.findById(id).orElseThrow(NotFoundException::new);
        model.addAttribute("book", book);
        return "deleteBook";
    }

    @PostMapping("/deleteBook")
    public String deleteBook(Book book) {
        bookService.deleteById(book.getId());
        return "redirect:/books";
    }

    @GetMapping("/browseBook")
    public String browseBookPage(@RequestParam("id") long id, Model model) {
        Book book = bookService.findById(id).orElseThrow(NotFoundException::new);
        List<Comment> comments = commentService.findByBook(book.getId());
        model.addAttribute("book", book);
        model.addAttribute("comments", comments);
        return "browseBook";
    }
}
