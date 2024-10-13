package com.example.kitchensinkboot.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/")
public class WelcomePage {

    @GetMapping
    public RedirectView redirect() {
        return new RedirectView("/index.xhtml");
    }
}