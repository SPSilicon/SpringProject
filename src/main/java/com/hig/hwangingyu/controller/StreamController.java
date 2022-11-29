package com.hig.hwangingyu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class StreamController {
    
    @GetMapping("/stream/play/{streamName}")
    public String stream(@PathVariable("streamName") String streamName ,Model model) {
        model.addAttribute("streamName", streamName);
        return "testmpd.html";
    }

    @GetMapping("/stream/share")
    public String share() {
        return "media.html";
    }


}
