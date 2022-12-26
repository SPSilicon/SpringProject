package com.hig.hwangingyu.utils;

import java.time.LocalDateTime;
import java.util.List;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JWTProvider {
    
    static public final String secret ="x#=o!FuR2RV9d5+Kj=koGMZwiRjuU2qcZX0zWA~^UVs)14UL0oU}sYv~*--=r-YeCh3nvCK+8.LetWEq!PZeM8BaF+i2D,x1~r?@";
    static private Algorithm algorithm = Algorithm.HMAC256(secret);
    
    public static String createJWT(String id,List<String> auth) {
        String token = JWT.create()
        .withClaim("username", id)
        .withExpiresAt(java.sql.Timestamp.valueOf(LocalDateTime.now().plusMinutes(5)))
        .withClaim("auth",auth)
        .sign(algorithm);

        return token;
    }


    public static DecodedJWT verify(String JWTtoken) {
        JWTVerifier verifier = JWT.require(algorithm)
        .withClaimPresence("username")
        //.withClaimPresence("password") 비밀번호는 없음
        .withClaimPresence("auth")
        .build();

        return verifier.verify(JWTtoken);
    }

    
    public static String getSecret() {
        return secret;
    }
}
