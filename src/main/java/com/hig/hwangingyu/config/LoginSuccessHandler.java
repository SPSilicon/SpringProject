package com.hig.hwangingyu.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;



public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

   
    private final String secret ="x#=o!FuR2RV9d5+Kj=koGMZwiRjuU2qcZX0zWA~^UVs)14UL0oU}sYv~*--=r-YeCh3nvCK+8.LetWEq!PZeM8BaF+i2D,x1~r?@";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        
        
        Algorithm algorithm = Algorithm.HMAC256(secret);
        Cookie jwtCookie = null;
        try {
            
            String token = JWT.create()
            .withClaim("username", authentication.getName())
            //.withClaim("password",authentication.getCredentials().toString())
            .withClaim("auth",authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
            .sign(algorithm);
            jwtCookie = new Cookie("AUTHORIZATION", token);
        } catch (JWTCreationException exception) {
            
        }

        if(jwtCookie!=null) {
            response.addCookie(jwtCookie);
        }
        
        super.onAuthenticationSuccess(request, response, authentication);
    }
    
    
    
}
