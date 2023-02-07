package com.hig.hwangingyu.controller;



import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

    @GetMapping("")
    public String welcome() {
        return "redirect:/home/0";
    }
    @GetMapping("/home")
    public String first() {
        
        return "redirect:/home/0";
    }

    @GetMapping("home/{pageNum}")
    public String home(@PathVariable int pageNum, Model model, HttpServletRequest request) {
        
        String curUsername="";
        Cookie[] cookies = request.getCookies();

        Optional<DecodedJWT> jwt = jwtProvider.getJWTfromCookies(cookies);
        if(jwt.isPresent()) {
            curUsername = jwt.get().getClaim("username").asString();
            model.addAttribute("username",curUsername);
        }

        Pageable pageable = PageRequest.of(pageNum, 20);
        Page<Article> page = articleService.findAll(pageable);
        int start = page.getNumber()-page.getNumber()%10;
        int end = start+10;
        if(start+10>page.getTotalPages()) {
            end = page.getTotalPages()-1;
        }
        
        model.addAttribute("articles", page.getContent());
        model.addAttribute("curPage", page.getNumber());
        model.addAttribute("hasNext", start-1>0);
        model.addAttribute("hasPrevious", start+10<end);
        model.addAttribute("start",start);
        model.addAttribute("end", end);
        
        return "index.html";
    }

    @GetMapping("live")
    public String live() {
        return "live.html";
    }
}
