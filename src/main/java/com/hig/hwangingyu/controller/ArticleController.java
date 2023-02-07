package com.hig.hwangingyu.controller;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import java.util.Optional;

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
    private final JWTProvider jwtProvider;

   
    public ArticleController(ArticleService articleService, JWTProvider jwtProvider) {
        this.articleService = articleService;
        this.jwtProvider  = jwtProvider;
    }

    @PostMapping("post/add")
    public String add(Article articleForm, HttpServletRequest request) {
        System.out.println(articleForm.getBody());

        Cookie[] cookies = request.getCookies();

        Optional<DecodedJWT> jwt = jwtProvider.getJWTfromCookies(cookies);
        if(jwt.isPresent()) {
            articleForm.setAuthor(jwt.get().getClaim("username").asString());
        }

        System.out.println(articleForm.getAuthor());
        articleService.uploadArticle(articleForm);
        return "redirect:/home";
    }

    @GetMapping("post/add")
    public String add() {
        return "addPost.html";
    }

    @GetMapping("post/update")
    public String update(@RequestParam Long id, Model model, HttpServletRequest request) {

        Article post = articleService.findById(id).orElse(null);
        if(post!=null) {
            String curUsername="";
            Cookie[] cookies = request.getCookies();
    
            Optional<DecodedJWT> jwt = jwtProvider.getJWTfromCookies(cookies);
            if(jwt.isPresent()) {
                curUsername = jwt.get().getClaim("username").asString();
                model.addAttribute("username",curUsername);
            }

            if(post.getAuthor().equals(curUsername)) {
                return "updatePost.html";
            }
        }


        return "redirect:/home";
    }

    @PostMapping("post/update")
    public String update(Article articleForm, Model model, HttpServletRequest request) {


        if(articleForm!=null) {
            String curUsername="";
            Cookie[] cookies = request.getCookies();
    
            Optional<DecodedJWT> jwt = jwtProvider.getJWTfromCookies(cookies);
            if(jwt.isPresent()) {
                curUsername = jwt.get().getClaim("username").asString();
                model.addAttribute("username",curUsername);
            }

            if(articleForm.getAuthor().equals(curUsername)) {
                articleService.updateArticle(articleForm);

                return "redirect:/home/post?id="+articleForm.getId();
            }
        }

        return "redirect:/home";
    }
    @GetMapping("post/delete")
    public String delete(@RequestParam Long id, Model model, HttpServletRequest request) {

        String curUsername="";
        Cookie[] cookies = request.getCookies();

        Optional<DecodedJWT> jwt = jwtProvider.getJWTfromCookies(cookies);
        if(jwt.isPresent()) {
            curUsername = jwt.get().getClaim("username").asString();
            model.addAttribute("username",curUsername);
        }

        Article article = articleService.read(id).orElse(null);
        if(article!=null) {
            if(article.getAuthor().equals(curUsername)) {
                articleService.deleleArticle(id);
            }
        }
        
        return "redirect:/home";
    }
    @GetMapping("home/post")
    public String read(@RequestParam Long id, Model model, HttpServletRequest request) {
        Article article = articleService.read(id).get();
        System.out.println(article.getBody());
        
        Cookie[] cookies = request.getCookies();
        Optional<DecodedJWT> jwt = jwtProvider.getJWTfromCookies(cookies);
        if(jwt.isPresent()) {
            model.addAttribute("username",jwt.get().getClaim("username").asString());
        }

        model.addAttribute("article", articleService.read(id).get());

        return "article.html";
    }

}
