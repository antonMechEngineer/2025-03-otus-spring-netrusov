package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.services.GenreService;

@Controller
@RequiredArgsConstructor
public class GenrePageController {

    private final GenreService genreService;

    @GetMapping("/genres")
    public String findAllAuthors(Model model) {
        return "allGenres";
    }
}
