package com.hig.hwangingyu.config;

import java.util.Arrays;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.hig.hwangingyu.controller.handler.LoginSuccessHandler;
import com.hig.hwangingyu.filter.JwtAuthenticationFilter;
import com.hig.hwangingyu.service.CustomOAuth2UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Autowired
    private DataSource dataSource;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests((authorizeRequests) -> authorizeRequests
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                        .permitAll()
                        .requestMatchers("/higlogin", "/", "/stream/vid/**", "/vendor/**", "/stream/play/*", "/member/register", "/home/**", "/koauth", "/streams", "/post")
                        .permitAll()
                        .anyRequest()
                        .hasAnyRole("USER"))

                .formLogin((formLogin) -> formLogin
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .loginPage("/higlogin")
                        .successHandler(loginSuccessHandler)
                        .failureUrl("/higlogin?fail")
                        .permitAll())

                .oauth2Login(login -> login
                        .successHandler(loginSuccessHandler)
                        .userInfoEndpoint()
                        .userService(customOAuth2UserService))

                .addFilterBefore(jwtAuthenticationFilter,
                        BasicAuthenticationFilter.class)

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .invalidateHttpSession(false).deleteCookies("AUTHORIZATION")
                        .logoutSuccessUrl("/home"))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));
                //.csrf((csrf) -> csrf.disable());




        //http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

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
