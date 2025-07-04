package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.rest.dto.AuthorDto;

@RequiredArgsConstructor
@RestController
public class AuthorController {

    private final AuthorRepository authorRepository;

    @GetMapping("/api/authors")
    public Flux<AuthorDto> getAllAuthors() {
        return authorRepository.findAll()
                .map(AuthorDto::toDto);
    }
}
