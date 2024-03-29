package com.hig.hwangingyu.controller;



import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public HomeController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/")
    public String welcome() {
        return "redirect:home";
    }

    @GetMapping("home")
    public String home(@RequestParam @Nullable Integer pageNum,
    @AuthenticationPrincipal String curUsername,
    @RequestParam @Nullable String query, 
    Model model, 
    HttpServletRequest request) {
        
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
        
        model.addAttribute("username",curUsername);
        model.addAttribute("articles", page.getContent());
        model.addAttribute("curPage", pageNum);
        model.addAttribute("hasNext", start > 0);
        model.addAttribute("hasPrevious", start + 10 < end);
        model.addAttribute("start", start);
        model.addAttribute("end", end);
        
        return "home.html";
    }

  
}
