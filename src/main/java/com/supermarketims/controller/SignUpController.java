package com.supermarketims.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SignUpController {

    @GetMapping("/signup")
    public String signUpPage() {
        return "signup"; // This will return signup.html from templates
    }
}
