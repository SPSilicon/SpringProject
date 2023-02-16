package com.hig.hwangingyu.controller;



import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.hig.hwangingyu.domain.Article;

import com.hig.hwangingyu.service.ArticleService;
import com.hig.hwangingyu.utils.JWTProvider;

@Controller
public class HomeController {
    private final ArticleService articleService;
    private final JWTProvider jwtProvider;
    
    public HomeController(ArticleService articleService, JWTProvider jwtProvider) {
        this.articleService = articleService;
        this.jwtProvider = jwtProvider;
    }

    @GetMapping("/")
    public String welcome() {
        return "redirect:/home?pageNum=0";
    }

    @GetMapping("home")
    public String home(@RequestParam @Nullable Integer pageNum, @RequestParam @Nullable String query, Model model, HttpServletRequest request) {
        
        Optional<DecodedJWT> jwt = jwtProvider.getJWTfromCookies(request.getCookies());
        Page<Article> page;

        if(pageNum==null) pageNum =0;

        if(query == null) {
            page = articleService.findAll(PageRequest.of(pageNum, 20));
        } else {
            page = articleService.searchArticle(PageRequest.of(pageNum,20), query);
            model.addAttribute("query", query);
        }

        
        int start = page.getNumber() - page.getNumber() % 10;
        int end = Math.min(start + 10, (int) page.getTotalPages() - 1);
    
        jwt.ifPresent(j -> model.addAttribute("username", j.getClaim("username").asString()));
        model.addAttribute("articles", page.getContent());
        model.addAttribute("curPage", pageNum);
        model.addAttribute("hasNext", start > 0);
        model.addAttribute("hasPrevious", start + 10 < end);
        model.addAttribute("start", start);
        model.addAttribute("end", end);
        
        return "index.html";
    }

    @GetMapping("live")
    public String live() {
        return "live.html";
    }
}
