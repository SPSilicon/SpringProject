package com.hig.hwangingyu.config;

import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import com.hig.hwangingyu.utils.JWTProvider;



public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        
        
        Cookie jwtCookie = new Cookie("AUTHORIZATION",
                                JWTProvider.createJWT(authentication.getName(),
                                authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()));
        jwtCookie.setMaxAge(10*60);

        if(jwtCookie!=null) {
            response.addCookie(jwtCookie);
        }
        
        super.onAuthenticationSuccess(request, response, authentication);
    }
    
    
    
}
