package com.hig.hwangingyu.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class CustomExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler({IllegalStateException.class})
    public String handleBadRequestException(WebRequest request, RuntimeException ex, Model model) {
        //TODO 메인화면에서 경고창뜨게 만들기
        log.info(ex.getMessage());
        return "redirect:/home";
    }
}
