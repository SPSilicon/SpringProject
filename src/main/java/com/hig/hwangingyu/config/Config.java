package com.hig.hwangingyu.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hig.hwangingyu.controller.handler.LoginSuccessHandler;
import com.hig.hwangingyu.controller.handler.SocketWebmHandler;
import com.hig.hwangingyu.filter.JwtAuthenticationFilter;
import com.hig.hwangingyu.repository.ArticleJdbcRepository;
import com.hig.hwangingyu.repository.ArticleRepository;
import com.hig.hwangingyu.repository.MemberRepository;
import com.hig.hwangingyu.repository.StreamRepository;
import com.hig.hwangingyu.repository.StreamJdbcRepository;
import com.hig.hwangingyu.repository.MemberJdbcRepository;
import com.hig.hwangingyu.service.MemberService;
import com.hig.hwangingyu.service.StreamService;
import com.hig.hwangingyu.service.CustomOAuth2UserService;
import com.hig.hwangingyu.service.ArticleService;
import com.hig.hwangingyu.utils.JWTProvider;

@Configuration
public class Config {

    private final DataSource dataSource;

    public Config(DataSource dataSource) { this.dataSource = dataSource;}


    @Bean
    public ArticleRepository articleRepository() {return new ArticleJdbcRepository(dataSource);}

    @Bean
    public MemberRepository memberRepository() {return new MemberJdbcRepository(dataSource);}

    @Bean
    public StreamRepository streamRepository() { return new StreamJdbcRepository(dataSource); }

    @Bean
    public ArticleService articleService() { return new ArticleService(articleRepository());}

    @Bean
    public MemberService memberService() { return new MemberService(memberRepository(),passwordEncoder());}

    @Bean
    public CustomOAuth2UserService customOAuth2UserService() { return new CustomOAuth2UserService(memberRepository(), passwordEncoder());}

    @Bean
    public StreamService streamService() { return new StreamService(streamRepository()); }

    @Bean
    public SocketWebmHandler socketWebmHandler() { return new SocketWebmHandler(streamService()); }

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    public JWTProvider jwtProvider() {return new JWTProvider(); }

    @Bean
    public LoginSuccessHandler loginSuccessHandler() { return new LoginSuccessHandler(jwtProvider()); }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {return new JwtAuthenticationFilter(jwtProvider());}

}
