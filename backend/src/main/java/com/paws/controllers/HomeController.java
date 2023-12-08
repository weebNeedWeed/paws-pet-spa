package com.paws.controllers;

import com.paws.services.urlShortens.UrlShortenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
    private final UrlShortenService urlShortenService;

    @Autowired
    public HomeController(UrlShortenService urlShortenService) {
        this.urlShortenService = urlShortenService;
    }

    @GetMapping("")
    public String index() {
        return "home/index";
    }

    @GetMapping("/shorten/{name}")
    public String redirectToRawUrl(@PathVariable("name") String name) {
        String raw = urlShortenService.getRawUrl(name);
        if(raw == null) {
            return "error/error-404";
        }

        return "redirect:" + raw;
    }
}
