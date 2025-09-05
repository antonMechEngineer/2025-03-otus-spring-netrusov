package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class UserPageController {

    @GetMapping("/profile")
    public String getCurrentProfile() {
        return "profile";
    }

    @GetMapping("/editProfile")
    public String editCurrentProfile(@RequestParam Long id, Model model) {
        model.addAttribute("userId", id);
        return "editProfile";
    }

}
