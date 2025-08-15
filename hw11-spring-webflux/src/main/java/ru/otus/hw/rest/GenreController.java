package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw.rest.dto.GenreDto;
import ru.otus.hw.services.GenreService;

@RequiredArgsConstructor
@RestController
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/api/genres")
    public Flux<GenreDto> getAllGenres() {
        return genreService.findAll().map(GenreDto::toDto);
    }
}