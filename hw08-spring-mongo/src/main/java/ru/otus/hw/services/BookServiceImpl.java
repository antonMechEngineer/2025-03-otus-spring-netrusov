package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    private final GenreRepository genreRepository;

    private final AuthorRepository authorRepository;

    @Override
    public Optional<Book> findById(long id) {
        return bookRepository.findById(String.valueOf(id));
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book insert(String title, long authorId, long genreId) {
        return save(0, title, authorId, genreId);
    }

    @Override
    public Book update(long id, String title, long authorId, long genreId) {
        return save(id, title, authorId, genreId);
    }

    @Override
    public void deleteById(long id) {
        commentRepository.deleteByBookId(String.valueOf(id));
        bookRepository.deleteById(String.valueOf(id));
    }

    private Book save(long id, String title, long authorId, long genreId) {
        Book book;
        var author = authorRepository.findById(String.valueOf(authorId))
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        var genre = genreRepository.findById(String.valueOf(genreId))
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %d not found".formatted(authorId)));
        if (id > 0) {
            bookRepository.findById(String.valueOf(id))
                    .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(id)));
            book = new Book(String.valueOf(id), title, author, genre);
        } else {
            book = new Book(null, title, author, genre);
        }
        return bookRepository.save(book);
    }
}
