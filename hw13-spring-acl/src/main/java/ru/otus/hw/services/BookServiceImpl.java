package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final AclServiceWrapperService aclServiceWrapperService;

    @Override
    @PreAuthorize("hasPermission(returnObject.orElse(null), 'READ')")
    public Optional<Book> findById(long id) {
        return bookRepository.findById(id);
    }

    @Override
    @PostFilter("hasPermission(filterObject, 'READ')")
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Transactional
    @Override
    @PreAuthorize("hasPermission(#book, 'WRITE')")
    public Book insert(Book book) {
        Book insertedBook = save(0, book.getTitle(), book.getAuthor().getId(), book.getGenre().getId());
        aclServiceWrapperService.createPermission(insertedBook, BasePermission.ADMINISTRATION);
        return insertedBook;
    }

    @Transactional
    @Override
    @PreAuthorize("hasPermission(#book, 'WRITE')")
    public Book update(Book book) {
        Book updatedBook = save(book.getId(), book.getTitle(), book.getAuthor().getId(), book.getGenre().getId());
        aclServiceWrapperService.createPermission(updatedBook, BasePermission.ADMINISTRATION);
        return updatedBook;
    }

    @Transactional
    @Override
    @PreAuthorize("hasPermission(#book, 'WRITE')")
    public void deleteById(long id) {
        Book book = bookRepository.findById(id).orElseThrow();
        bookRepository.delete(book);
    }

    private Book save(long id, String title, long authorId, long genreId) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        var genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %d not found".formatted(genreId)));
        var book = new Book(id, title, author, genre);
        return bookRepository.save(book);
    }
}
