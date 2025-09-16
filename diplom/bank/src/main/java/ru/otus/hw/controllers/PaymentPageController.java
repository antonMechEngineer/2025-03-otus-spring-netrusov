package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@SuppressWarnings("unused")
@Controller
@RequiredArgsConstructor
public class PaymentPageController {

    @GetMapping("/payments")
    public String findClientPayments() {
        return "payments";
    }

}
