package com.example.oauthsession.config;

import com.example.oauthsession.oauth2.CustomClientRegistrationRepo;
import com.example.oauthsession.oauth2.CustomOAuth2AuthorizedClientService;
import com.example.oauthsession.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomClientRegistrationRepo customClientRegistrationRepo;
    private final CustomOAuth2AuthorizedClientService customOAuth2AuthorizedClientService;
    private final JdbcTemplate jdbcTemplate;


    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService, CustomClientRegistrationRepo customClientRegistrationRepo, CustomOAuth2AuthorizedClientService customOAuth2AuthorizedClientService, JdbcTemplate jdbcTemplate) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.customClientRegistrationRepo = customClientRegistrationRepo;
        this.customOAuth2AuthorizedClientService = customOAuth2AuthorizedClientService;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf((csrf) -> csrf.disable());

        http
                .formLogin((login) -> login.disable());

        http
                .httpBasic((basic) -> basic.disable());

        http
                .oauth2Login((oauth2) -> oauth2
                        .loginPage("/login")
                        .clientRegistrationRepository(customClientRegistrationRepo.clientRegistrationRepository())
                        .authorizedClientService(customOAuth2AuthorizedClientService.oAuth2AuthorizedClientService(jdbcTemplate, customClientRegistrationRepo.clientRegistrationRepository()))
                        .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                                .userService(customOAuth2UserService)));

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/oauth2/**", "/login/**").permitAll() // 아무나 접근 가능
                        .anyRequest().authenticated()); // 그 외는 로그인을 한 사람만 접근 가능

        return http.build();
    }
}