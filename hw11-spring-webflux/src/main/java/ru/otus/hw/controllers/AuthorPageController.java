package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AuthorPageController {

    @GetMapping("/authors")
    public String findAllAuthors() {
        return "allAuthors";
    }

}
