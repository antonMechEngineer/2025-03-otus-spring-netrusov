package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class BookPageController {

    @GetMapping("/books")
    public String findAllBooks(Model model) {
        return "allBooks";
    }

    @GetMapping("/insertBook")
    public String insertBookForm(@RequestParam(required = false) Long id, Model model) {
        model.addAttribute("bookId", id);
        return "insertBook";
    }

    @GetMapping("/editBook")
    public String editBookForm(@RequestParam Long id, Model model) {
        model.addAttribute("bookId", id);
        return "editBook";
    }

    @GetMapping("/browseBook")
    public String browseBookDetails(@RequestParam Long id, Model model) {
        model.addAttribute("bookId", id);
        return "browseBook";
    }

    @GetMapping("/deleteBook")
    public String confirmDeleteBook(@RequestParam Long id, Model model) {
        model.addAttribute("bookId", id);
        return "deleteBook";
    }
}
