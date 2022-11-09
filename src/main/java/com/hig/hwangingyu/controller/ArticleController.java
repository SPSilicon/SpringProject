package com.hig.hwangingyu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hig.hwangingyu.domain.Article;
import com.hig.hwangingyu.service.ArticleService;

@Controller
public class ArticleController {
    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping("post/add")
    public String add(Article articleForm) {
        System.out.println(articleForm.getBody());
        articleService.uploadArticle(articleForm);
        return "redirect:/";
    }

    @GetMapping("post/add")
    public String add() {
        return "addPost.html";
    }

    @GetMapping("post")
    public String read(@RequestParam Long id, Model model) {
        Article article = articleService.read(id).get();
        System.out.println(article.getBody());
        model.addAttribute("article", articleService.read(id).get());

        return "article.html";
    }

}
