package com.hig.hwangingyu.config;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.hig.hwangingyu.utils.JWTProvider;

public class JwtAuthenticationFilter extends OncePerRequestFilter{


   
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
       
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for(Cookie cookie : cookies) {
                if(cookie.getName().compareTo("AUTHORIZATION")==0) {         
 
  
                    try {
                        DecodedJWT jwt = JWTProvider.verify(cookie.getValue());
                        
                        List<GrantedAuthority> auths = new ArrayList<>(); 
                        for(GrantedAuthority auth : jwt.getClaim("auth").asList(String.class).stream().map(SimpleGrantedAuthority::new).toList()) {
                            auths.add(auth);
                        }
                        
                        Authentication authentication = 
                            new UsernamePasswordAuthenticationToken(jwt.getClaim("username").asString(),
                                                                    "[PROTECTED]",
                                                                    auths);
                        
                        SecurityContext context = SecurityContextHolder.createEmptyContext();                 
                        context.setAuthentication(authentication);
                        
                        cookie.setValue(JWTProvider.createJWT(jwt.getClaim("username").asString(),
                                                                jwt.getClaim("auth").asList(String.class)));
                        cookie.setMaxAge(10*60);
                        SecurityContextHolder.setContext(context);
                    } catch(Exception e ){
                        cookie.setMaxAge(0);
                        cookie.setPath("/");
                    }
          
                    break;
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    

    

}
