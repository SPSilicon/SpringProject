package com.hig.hwangingyu.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;







//@Configuration
@EnableWebSecurity
public class OAuth2LoginConfig {
    
    @Value("${oauth.kakao.clientid}")
    private String kakaoClientId;
    @Value("${oauth.kakao.secret}")
    private String kakaoClientSecret;
    
    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
    return new InMemoryClientRegistrationRepository(this.kakaoClientRegistration());
    }

    private ClientRegistration kakaoClientRegistration() {
    return ClientRegistration.withRegistrationId("kakao")
        .clientId(kakaoClientId)//"12a6f1f63767372dc771760ff2e447fb")
        .clientSecret(kakaoClientSecret)//"i1TIFGvRn4W3K22DPUYJWVGXeaHY5Bmo")
        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
        .redirectUri("{baseUrl}/login/oauth2/code/")
        .scope("profile_nickname")
        .authorizationUri("https://kauth.kakao.com/oauth/authorize")
        .tokenUri("https://kauth.kakao.com/oauth/token")
        .userInfoUri("https://kapi.kakao.com/v2/user/me")
        .userNameAttributeName("id")
        .clientName("Kakao")
        .build();
    }
}
