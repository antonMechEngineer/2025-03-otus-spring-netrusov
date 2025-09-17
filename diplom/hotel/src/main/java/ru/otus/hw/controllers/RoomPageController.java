package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@SuppressWarnings("unused")
@Controller
@RequiredArgsConstructor
public class RoomPageController {

    @GetMapping("/rooms")
    public String findAllRooms() {
        return "rooms";
    }

    @GetMapping("/createRoom")
    public String createRoom() {
        return "createRoom";
    }

    @GetMapping("/editRoom")
    public String editRoom(@RequestParam Long id, Model model) {
        model.addAttribute("id", id);
        return "editRoom";
    }

}
