package ru.otus.hw.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

@RequiredArgsConstructor
@Service
public class MigrationService {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    @PostConstruct
    public void init() {
        authorRepository.deleteAll();
        genreRepository.deleteAll();
        bookRepository.deleteAll();
        commentRepository.deleteAll();

        Genre genre = new Genre(null, "genre");
        Author author = new Author(null, "author");
        Book book = new Book(null, "bookTitle", author, genre);
        Book updatedBook = new Book(null, "bookTitle" + 4, author, genre);
        Comment comment = new Comment(null, "comment", book);

        genreRepository.save(genre);
        authorRepository.save(author);
        bookRepository.save(book);
        bookRepository.save(updatedBook);
        commentRepository.save(comment);
    }
}

