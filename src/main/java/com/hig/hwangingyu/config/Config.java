package com.hig.hwangingyu.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hig.hwangingyu.repository.ArticleJdbcRepository;
import com.hig.hwangingyu.repository.ArticleRepository;
import com.hig.hwangingyu.repository.MemberRepository;
import com.hig.hwangingyu.repository.MemberjdbcRepository;
import com.hig.hwangingyu.service.MemberService;
import com.hig.hwangingyu.service.ArticleService;

@Configuration
public class Config {

    private final DataSource dataSource;

    public Config(DataSource dataSource) { this.dataSource = dataSource;}



    @Bean
    public ArticleRepository articleRepository() {return new ArticleJdbcRepository(dataSource);}

    @Bean
    public MemberRepository memberRepository() {return new MemberjdbcRepository(dataSource);}

    @Bean
    public ArticleService articleService() { return new ArticleService(articleRepository());}

    @Bean
    public MemberService memberService() { return new MemberService(memberRepository());}

}
