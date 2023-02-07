package com.hig.hwangingyu.utils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Value;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;


public class JWTProvider {

    @Value("${jwt.secret}")
    private String secret;
    
    public String encode(String id,List<String> auth) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        String token = JWT.create()
        .withClaim("username", id)
        .withExpiresAt(java.sql.Timestamp.valueOf(LocalDateTime.now().plusMinutes(30)))
        .withClaim("auth",auth)
        .sign(algorithm);

        return token;
    }

    public Optional<DecodedJWT> decode(String JWTtoken) {
        //JWTVerifier verifier = JWT.require(algorithm)
        //.withClaimPresence("username")
        //.withClaimPresence("password") 비밀번호는 없음
        //.withClaimPresence("auth")
        //.build();

        DecodedJWT ret = JWT.decode(JWTtoken);
        if(ret.getExpiresAt().before(new Date())) {
            return Optional.empty();
        }

        
        return Optional.ofNullable(ret);
    }

    public Optional<DecodedJWT> getJWTfromCookies(Cookie[] cookies) {

        Optional<DecodedJWT> ret = Optional.empty();
        if(cookies!= null) {
                for(Cookie c : cookies) {
                    if(c.getName().compareTo("AUTHORIZATION")==0) {
                        ret = decode(c.getValue());
                    }
            }
        }
        return ret;
    }
}
