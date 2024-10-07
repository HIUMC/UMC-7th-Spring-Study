package com.example.testsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 스프링 시큐리티에 의해 관리됨
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 특정 경로로 요청이 왔을 때 그 경로는 모든 사용자에게 오픈시켜주고,
        // 특정 admin 경로는 admin 사용자에게만 오픈시켜주고,
        // 다른 경로는 로그인을 진행해야 접근할 수 있다.

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/login").permitAll() // 특정 경로에 대해 어떤 작업을 진행하고 싶다는 설정
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated() // 위에서 처리하지 못한 나머지 경로 처리 -> 로그인한 사용자만 접근할 수 있도록
                );

        http
                .formLogin((auth) -> auth.loginPage("/login") // 우리가 설정해둔 로그인 페이지 경로가 어디있는지 설정해줌
                    .loginProcessingUrl("/loginProc")
                    .permitAll()
                );

        http
                .csrf((auth) -> auth.disable());

        return http.build();
    }
}
