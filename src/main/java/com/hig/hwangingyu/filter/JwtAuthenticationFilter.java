package com.hig.hwangingyu.filter;

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


    private final JWTProvider jwtProvider;

    public JwtAuthenticationFilter(JWTProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
       
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for(Cookie cookie : cookies) {
                if(cookie.getName().equals("AUTHORIZATION")) {         
 

                        DecodedJWT jwt = jwtProvider.decode(cookie.getValue()).orElse(null);
                        
                        if(jwt != null) {
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
                            
                            //Timestamp t = Timestamp.valueOf(LocalDateTime.now().plusMinutes(2));
                            //if(jwt.getExpiresAt().before(t)) {
                                Cookie jwtCookie = new Cookie("AUTHORIZATION",
                                jwtProvider.encode(authentication.getName(),
                                authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()));

                                jwtCookie.setPath("/");
                                jwtCookie.setHttpOnly(true);
                                jwtCookie.setSecure(true);

                                response.addCookie(jwtCookie);
                            //}
    
                            SecurityContextHolder.setContext(context);
                        } else {
                            cookie.setMaxAge(0);
                            cookie.setPath("/");
                            cookie.setHttpOnly(true);
                            cookie.setSecure(true);
                            response.addCookie(cookie);
                        }           
                    break;
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    

    

}
