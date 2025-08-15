package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.rest.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/api/authors")
    public List<AuthorDto> getAllAuthors() {
        List<AuthorDto> authors = authorService.findAll().stream().map(AuthorDto::toDto).toList();
        if (authors.isEmpty()) {
            throw new EntityNotFoundException("Authors not found!");
        }
        return authors;
    }
}
