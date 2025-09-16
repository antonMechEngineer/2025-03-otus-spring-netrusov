package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@SuppressWarnings("unused")
@Controller
@RequiredArgsConstructor
public class OrderPageController {

    @GetMapping("/orders")
    public String findClientOrders() {
        return "orders";
    }

    @GetMapping("/createOrder")
    public String createOrder(@RequestParam(required = false) Long id, Model model) {
        model.addAttribute("orderId", id);
        return "createOrder";
    }

    @GetMapping("/editOrder")
    public String editOrder(@RequestParam Long id, Model model) {
        model.addAttribute("orderId", id);
        return "editOrder";
    }


}
