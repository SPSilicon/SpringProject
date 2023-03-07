package com.hig.hwangingyu.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@ControllerAdvice
public class CustomExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler({IllegalStateException.class})
    public String handleBadRequestException(WebRequest request, RuntimeException ex, RedirectAttributes redirectAttribute) {
        
        log.info(ex.getMessage());
        redirectAttribute.addFlashAttribute("error", ex.getMessage());
        String target = request.getHeader("referer") == null? "home" : request.getHeader("Referer");
        return "redirect:/"+target;
    }
}
