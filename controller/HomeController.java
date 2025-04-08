package com.supermarketims.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "redirect:/login";  // Redirect to /login
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";  // Loads login.html from templates
    }
}
