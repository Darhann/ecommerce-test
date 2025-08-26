package com.example.ecommerce.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebPageController {

    @GetMapping({"/", "/home", "/products"})
    public String productsPage() {
        return "products"; // возвращает templates/products.html
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // возвращает templates/login.html
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register"; // возвращает templates/register.html
    }

    @GetMapping("/cart")
    public String cartPage() {
        return "cart"; // возвращает templates/cart.html
    }
}