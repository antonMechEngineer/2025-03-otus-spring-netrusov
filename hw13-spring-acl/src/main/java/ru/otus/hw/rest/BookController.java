package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import ru.otus.hw.rest.dto.BookDto;
import ru.otus.hw.rest.dto.CommentDto;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;

@RequiredArgsConstructor
@RestController
public class BookController {

    private final BookService bookService;

    private final CommentService commentService;


    @GetMapping("/api/books")
    public ResponseEntity<List<BookDto>> findAllBooks() {
        return ResponseEntity.ok(bookService.findAll().stream().map(BookDto::toDto).toList());
    }

    @GetMapping("/api/books/{id}")
    public ResponseEntity<BookDto> findBookById(@PathVariable Long id) {
        return bookService.findById(id).map(BookDto::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/api/books")
    public ResponseEntity<BookDto> createBook(@RequestBody BookDto bookDto) {
        BookDto createdBookDto = BookDto.toDto(bookService.insert(bookDto.toDomainObject()));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBookDto);
    }

    @PutMapping("/api/books/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable Long id, @RequestBody BookDto updatedBookDto) {
        if (bookService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        BookDto result = BookDto.toDto(bookService.update(updatedBookDto.toDomainObject()));
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/api/books/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        if (bookService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/books/{id}/comments")
    public ResponseEntity<List<CommentDto>> getBookComments(@PathVariable Long id) {
        List<CommentDto> bookComments = commentService.findByBook(id).stream().map(CommentDto::toDto).toList();
        return ResponseEntity.ok(bookComments);
    }
}
