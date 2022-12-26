package com.hig.hwangingyu.controller;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.hig.hwangingyu.domain.Article;
import com.hig.hwangingyu.service.ArticleService;
import com.hig.hwangingyu.utils.JWTProvider;

@Controller
public class ArticleController {
    private final ArticleService articleService;

   
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping("post/add")
    public String add(Article articleForm, HttpServletRequest request) {
        System.out.println(articleForm.getBody());
        for(Cookie c : request.getCookies()) {
            if(c.getName().compareTo("AUTHORIZATION")==0) {
                DecodedJWT jwt = JWTProvider.verify(c.getValue());
                articleForm.setAuthor(jwt.getClaim("username").asString());
            }
        }
        System.out.println(articleForm.getAuthor());
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
