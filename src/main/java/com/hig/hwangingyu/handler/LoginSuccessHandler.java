package com.hig.hwangingyu.handler;

import java.io.IOException;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import com.hig.hwangingyu.utils.JWTProvider;




public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {


    private final JWTProvider jwtProvider;

    public LoginSuccessHandler(JWTProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        
            
        Cookie jwtCookie = new Cookie("AUTHORIZATION",
                                jwtProvider.encode(authentication.getName(),
                                authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()));
    
        if(jwtCookie!=null) {
            jwtCookie.setPath("/");
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(true);
            response.addCookie(jwtCookie);
        }
        setDefaultTargetUrl("/");
        RequestCache requestCache = new HttpSessionRequestCache();
        RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if(savedRequest != null){
            // 인증 받기 전 url로 이동하기
            String targetUrl = savedRequest.getRedirectUrl();
            redirectStrategy.sendRedirect(request,response,targetUrl);
        }else{
            // 기본 url로 가도록 함
           redirectStrategy.sendRedirect(request,response,getDefaultTargetUrl());
        }
        
        super.onAuthenticationSuccess(request, response, authentication);
    }
    
    
    
}
