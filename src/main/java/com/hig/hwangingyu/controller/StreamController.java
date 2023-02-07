package com.hig.hwangingyu.controller;



import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.hig.hwangingyu.domain.Stream;
import com.hig.hwangingyu.service.StreamService;
import com.hig.hwangingyu.utils.JWTProvider;

@Controller
public class StreamController {
    
    private final StreamService streamService;
    private final JWTProvider jwtProvider;
    public StreamController(StreamService streamService, JWTProvider jwtProvider) {
        this.streamService = streamService;
        this.jwtProvider = jwtProvider;
    }

    @GetMapping("/stream/play/{streamName}")
    public String stream(@PathVariable("streamName") String streamName ,Model model) {
        model.addAttribute("streamName", streamName);
        return "testmpd.html";
    }

    @GetMapping("/stream/share")
    public String share(Model model, HttpServletRequest request) {

        String curUsername="";
        Cookie[] cookies = request.getCookies();

        Optional<DecodedJWT> jwt = jwtProvider.getJWTfromCookies(cookies);
        if(jwt.isPresent()) {
            curUsername = jwt.get().getClaim("username").asString();
            model.addAttribute("username",curUsername);
        }
        
        return "media.html";
    }

    @GetMapping("/streams") 
    public String streams(@RequestParam int pageNum, Model model ,HttpServletRequest request) {

        String curUsername="";
        Cookie[] cookies = request.getCookies();

        Optional<DecodedJWT> jwt = jwtProvider.getJWTfromCookies(cookies);
        if(jwt.isPresent()) {
            curUsername = jwt.get().getClaim("username").asString();
            model.addAttribute("username",curUsername);
        }

        Page<Stream> page = streamService.findAll(PageRequest.of(pageNum,16));
        model.addAttribute("streams", page.getContent());

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

        return "streams.html";
    }




}
