package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SuppressWarnings("unused")
@Controller
@RequiredArgsConstructor
public class OrderPageController {

    @GetMapping("/orders")
    public String findClientOrders() {
        return "orders";
    }

    @GetMapping("/createOrder")
    public String createOrder() {
        return "createOrder";
    }
}
