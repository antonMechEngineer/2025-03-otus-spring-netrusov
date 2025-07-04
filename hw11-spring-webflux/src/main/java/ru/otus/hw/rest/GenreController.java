package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.rest.dto.GenreDto;

@RequiredArgsConstructor
@RestController
public class GenreController {

    private final GenreRepository genreRepository;

    @GetMapping("/api/genres")
    public Flux<GenreDto> getAllGenres() {
        return genreRepository.findAll()
                .map(GenreDto::toDto);
    }
}