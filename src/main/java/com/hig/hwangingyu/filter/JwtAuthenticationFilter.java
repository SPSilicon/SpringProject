package com.hig.hwangingyu.filter;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Arrays;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



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
       
        Optional<Cookie[]> cookies = Optional.ofNullable(request.getCookies());
        if(cookies.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        Optional<Cookie> authorizationCookie = Arrays.stream(cookies.get()).filter(cookie -> cookie.getName().equals("AUTHORIZATION")).findFirst();
        if(!authorizationCookie.isPresent()) {
            filterChain.doFilter(request, response);
            return;
        }

        Optional<DecodedJWT> jwt = jwtProvider.decode(authorizationCookie.get().getValue());
        if(jwt.isEmpty()) {
            authorizationCookie.get().setMaxAge(0);
            authorizationCookie.get().setPath("/");
            authorizationCookie.get().setHttpOnly(true);
            authorizationCookie.get().setSecure(true);
            response.addCookie(authorizationCookie.get());
            return;
        }


        List<SimpleGrantedAuthority> auths = new ArrayList<>(); 
        auths =jwt.get().getClaim("auth").asList(String.class).stream().map(SimpleGrantedAuthority::new).toList();
        
        Authentication authentication = 
            new UsernamePasswordAuthenticationToken(jwt.get().getClaim("username").asString(),
                                                    "[PROTECTED]",
                                                    auths);
        
        SecurityContext context = SecurityContextHolder.createEmptyContext();                 
        context.setAuthentication(authentication);
        
        Cookie jwtCookie = new Cookie("AUTHORIZATION",
        jwtProvider.encode(authentication.getName(),
        authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()));

        jwtCookie.setPath("/");
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);

        response.addCookie(jwtCookie);

        SecurityContextHolder.setContext(context);

        filterChain.doFilter(request, response);
    }

    

    

}
