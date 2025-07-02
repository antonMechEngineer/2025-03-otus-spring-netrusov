package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.rest.dto.GenreDto;
import ru.otus.hw.rest.exceptions.NotFoundException;
import ru.otus.hw.services.GenreService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/api/genres")
    public List<GenreDto> getAllGenres() {
        List<GenreDto> genres = genreService.findAll().stream().map(GenreDto::toDto).toList();
        if (genres.isEmpty()) {
            throw new NotFoundException("Genres not found!");
        }
        return genres;
    }
}