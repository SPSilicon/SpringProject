package com.hig.hwangingyu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.hig.hwangingyu.domain.Article;

import com.hig.hwangingyu.service.ArticleService;

@Controller
public class HomeController {
    private final ArticleService articleService;

    @Autowired
    public HomeController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("")
    public String home(Model model) {
        List<Article> list = articleService.findAll();
        model.addAttribute("articles", list);
        return "index.html";
    }

    @GetMapping("live")
    public String live() {
        return "live.html";
    }
}
