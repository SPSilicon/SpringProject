package com.hig.hwangingyu.controller;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import java.util.Optional;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public String add(Article articleForm,@AuthenticationPrincipal String curUsername, HttpServletRequest request) {
        System.out.println(articleForm.getBody());

        articleForm.setAuthor(curUsername);

        System.out.println(articleForm.getAuthor());
        articleService.uploadArticle(articleForm);
        return "redirect:/home";
    }

    @GetMapping("post/add")
    public String add() {
        return "addPost.html";
    }

    @GetMapping("post/update")
    public String update(@RequestParam Long id,@AuthenticationPrincipal String curUsername, Model model, HttpServletRequest request) {

        Article post = articleService.findById(id).orElse(null);

        if(post!=null &&post.getAuthor().equals(curUsername)) {
            model.addAttribute("username",curUsername);
            return "updatePost.html";
        } else {
            throw new IllegalStateException(curUsername+"안됨!");
        }
    }

    @PostMapping("post/update")
    public String update(Article articleForm,@AuthenticationPrincipal String curUsername, Model model, HttpServletRequest request) {

        if(articleForm.getAuthor().equals(curUsername)) {
            articleService.updateArticle(articleForm);
            return "redirect:/home/post?id="+articleForm.getId();
        } else {
            throw new IllegalStateException(curUsername+"안됨!");
        }

    }
    
    @GetMapping("post/delete")
    public String delete(@RequestParam Long id, @AuthenticationPrincipal String curUsername, Model model, HttpServletRequest request) {
        model.addAttribute("username",curUsername);

        Article article = articleService.read(id).orElse(null);
        if(article!=null && article.getAuthor().equals(curUsername)) {
            articleService.deleleArticle(id);
        } else {
            throw new IllegalStateException(curUsername+"안됨!");
        }
        
        return "redirect:/home";
    }


    @GetMapping("post")
    public String read(@RequestParam Long id, @AuthenticationPrincipal String curUsername, Model model, HttpServletRequest request) {
        Article article = articleService.read(id).get();
        System.out.println(article.getBody());
        model.addAttribute("username",curUsername);

        model.addAttribute("article", articleService.read(id).get());

        return "article.html";
    }

}
