package com.hig.hwangingyu.controller;



import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


import com.hig.hwangingyu.domain.Stream;
import com.hig.hwangingyu.service.StreamService;


@Controller
public class StreamController {
    
    private final StreamService streamService;

    public StreamController(StreamService streamService) {
        this.streamService = streamService;
    }

    @GetMapping("/stream/play/{streamName}")
    public String stream(@PathVariable("streamName") String streamName ,Model model) {
        model.addAttribute("streamName", streamName);
        return "testmpd.html";
    }

    @GetMapping("/stream/share")
    public String share(Model model,@AuthenticationPrincipal String curUsername, HttpServletRequest request) {

        model.addAttribute("username",curUsername);
    
        return "media.html";
    }

    @GetMapping("/streams") 
    public String streams(@RequestParam @Nullable Integer pageNum, @RequestParam @Nullable String query,@AuthenticationPrincipal String curUsername, Model model ,HttpServletRequest request) {

        if(pageNum==null) pageNum=0;

        model.addAttribute("username",curUsername);

        Page<Stream> page;
        if(query == null) {
            page = streamService.findAll(PageRequest.of(pageNum,16));
        } else {
            page = streamService.searchByStreamer(PageRequest.of(pageNum,16), query);
        }
         
        model.addAttribute("streams", page.getContent());

        int start = page.getNumber()-page.getNumber()%10;
        int end = start+10;
        if(start+10>page.getTotalPages()) {
            end = page.getTotalPages()-1;
        }

        model.addAttribute("query", query);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("articles", page.getContent());
        model.addAttribute("curPage", page.getNumber());
        model.addAttribute("hasNext", start-1>0);
        model.addAttribute("hasPrevious", start+10<end);
        model.addAttribute("start",start);
        model.addAttribute("end", end);

        return "streams.html";
    }




}
