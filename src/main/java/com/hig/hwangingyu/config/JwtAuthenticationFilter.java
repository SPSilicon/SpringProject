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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JwtAuthenticationFilter extends OncePerRequestFilter{


    private final String secret ="x#=o!FuR2RV9d5+Kj=koGMZwiRjuU2qcZX0zWA~^UVs)14UL0oU}sYv~*--=r-YeCh3nvCK+8.LetWEq!PZeM8BaF+i2D,x1~r?@";
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for(Cookie cookie : cookies) {
                if(cookie.getName().compareTo("AUTHORIZATION")==0) {         
                    Algorithm algorithm = Algorithm.HMAC256(secret);
                    JWTVerifier verifier = JWT.require(algorithm)
                        .withClaimPresence("username")
                        //.withClaimPresence("password")
                        .withClaimPresence("auth")
                        .build();
    
                    DecodedJWT jwt = verifier.verify(cookie.getValue());
                    
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
    
                    SecurityContextHolder.setContext(context);              
                    break;
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    

    

}
