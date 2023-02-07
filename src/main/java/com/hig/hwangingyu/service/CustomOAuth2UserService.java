package com.hig.hwangingyu.service;


import java.util.Optional;

import java.util.UUID;


import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

import org.springframework.security.oauth2.core.user.OAuth2User;

import org.springframework.util.Assert;

import com.hig.hwangingyu.domain.KakaoOAuth2User;
import com.hig.hwangingyu.domain.Member;
import com.hig.hwangingyu.repository.MemberRepository;


public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    
    private final PasswordEncoder passwordEncoder;
    
    private static final Member.Builder memberBuilder = new Member.Builder();


    public CustomOAuth2UserService(MemberRepository memberRepository,PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        
        Assert.notNull(userRequest, "userRequest cannot be null");

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();    
        
        if(provider.equals("google")){
            //oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }
        else if(provider.equals("naver")){
           // oAuth2UserInfo = new NaverUserInfo(oAuth2User.getAttributes());
        }
        else if(provider.equals("kakao")){
           oAuth2User = new KakaoOAuth2User(oAuth2User);
        }

        Optional<Member> check = memberRepository.findByName(oAuth2User.getName());
       
        //DB에 없는 사용자라면 회원가입처리
        if(check.isEmpty()){

            String uuid = UUID.randomUUID().toString().substring(0, 6);
            String password = passwordEncoder.encode(uuid); 

            Member member = memberBuilder
            .setName(oAuth2User.getName())
            .setPasswd(passwordEncoder.encode(password))
            .build();
            
            UserDetails user = User.builder()
            .username(member.getName())
            .password(member.getPasswd())
            .roles("USER")
            .build();
            
            memberRepository.register(user);
        }

        //oAuth2User.getAttributes().put("username",username);

        return oAuth2User;
    }


        
    

}