package com.hig.hwangingyu.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;


public class KakaoOAuth2User implements OAuth2User{

	private final List<GrantedAuthority> authorities;

	private final Map<String, Object> attributes;

    private final Map<String, Object> kakaoAccount;
	private final String username;
    
    public KakaoOAuth2User(OAuth2User oAuth2User) {
        this.attributes = oAuth2User.getAttributes();
        this.authorities = new ArrayList<>();
        GrantedAuthority defaultAuth = new SimpleGrantedAuthority("ROLE_USER");
        this.authorities.add(defaultAuth);
        for(GrantedAuthority auth :oAuth2User.getAuthorities()) {
            this.authorities.add(auth);
        }
        

        Object obj = oAuth2User.getAttribute("kakao_account");
        if(obj instanceof Map<?,?>) {
            this.kakaoAccount = ((Map<String, Object>)obj);
        } else {
            this.kakaoAccount = null;
        }
        
        this.username="kakao_"+this.getAttribute("id");
    }

    @Override
    public Map<String, Object> getAttributes() {

        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return this.authorities;
    }

    @Override
    public String getName() {

        return username;
    }


    
}
