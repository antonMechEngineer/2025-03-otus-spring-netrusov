package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SuppressWarnings("unused")
@Controller
@RequiredArgsConstructor
public class PaymentPageController {

    @GetMapping("/payments")
    public String findClientPayments() {
        return "payments";
    }

}
