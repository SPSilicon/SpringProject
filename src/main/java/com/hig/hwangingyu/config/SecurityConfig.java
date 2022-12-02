package com.hig.hwangingyu.config;

import java.util.Arrays;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Autowired
    private DataSource dataSource;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        
        http
                .authorizeRequests((authorizeRequests) -> authorizeRequests
                        .antMatchers("/html/**", "/css/**", "/img/**", "/js/**", "/vendor/**", "/scss/**", "/stream/**","/member/register")
                        .permitAll()
                        .antMatchers("/**").hasRole("USER"))
                .formLogin((formLogin) -> formLogin
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .loginPage("/login")
                        .successHandler(new LoginSuccessHandler())
                        .failureUrl("/login?error")
                        .permitAll())
                        .addFilterBefore( new JwtAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class)
                        .cors().configurationSource(corsConfigurationSource())
                        .and()
                        .csrf().disable();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource);
    }

    

    /*
     * @Bean
     * UserDetailsManager users(DataSource dataSource) {
     * UserDetails user = User.builder()
     * .username("user")
     * .password((
     * "{bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW"))
     * .roles("USER")
     * .build();
     * JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
     * users.createUser(user);
     * return users;
     * }
     */

}
