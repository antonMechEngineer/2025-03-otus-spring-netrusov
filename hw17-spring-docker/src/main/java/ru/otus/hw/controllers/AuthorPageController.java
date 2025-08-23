package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.services.AuthorService;

@Controller
@RequiredArgsConstructor
public class AuthorPageController {

    private final AuthorService authorService;

    @GetMapping("/authors")
    public String findAllAuthors(Model model) {
        return "allAuthors";
    }
}
