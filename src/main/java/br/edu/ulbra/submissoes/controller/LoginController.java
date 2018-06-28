package br.edu.ulbra.submissoes.controller;

import org.springframework.web.bind.annotation.PostMapping;

public class LoginController {
    @PostMapping("/login")
    public String login () {
        return "redirect:/";
    }
}
